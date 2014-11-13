package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.ProcessEvent;
import com.heap3d.application.events.StartDefinition;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.heap3d.application.events.ProcessEventType.DEBUG_MSG;
import static com.heap3d.application.utilities.ProcessState.*;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Map.Entry;

/**
 * Created by oskar on 11/11/14.
 */
public class DebuggedProcess {

    private StartDefinition _definition;
    private IVirtualMachineProvider _provider;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private EventBus _eventBus;
    private Map<String, Entry<Vector<String>, Vector<String>>> _cachedPoints;
    private ThreadReference _threadRef;

    public DebuggedProcess(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _definition = definition;
        _provider = provider;
        _eventBus = eventBus;
        _cachedPoints = new HashMap<>();
        _eventBus.register(this);
    }

    public void dispose() {
        _state = STOPPED;
        _process.destroy();
    }

    public void start() {
        runProcessAndEstablishConnection();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new StreamListener(_eventBus, _process.getInputStream(), _process.getErrorStream()));
        service.shutdown();
    }

    private void runProcessAndEstablishConnection() {
        int port = getRandomPort();
        DProcess cp = _provider.establish(port, () -> _definition.buildProcess(port));
        _instance = cp.virtualMachine;
        _process = cp.process;
        _state = RUNNING;
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }

    public void pause() {
        if (_instance != null && _state == RUNNING) {
            _state = PAUSED;
            _instance.suspend();
        }
    }

    public void resume() {
        if (_instance != null && _state == PAUSED) {
            _state = RUNNING;
            _threadRef = null;
            _instance.resume();
        }
    }

    public boolean waitForEvents() throws InterruptedException {
        if(_instance != null && _state == RUNNING) {
            EventQueue vmQueue = _instance.eventQueue();
            EventSet set = vmQueue.remove();
            for(Event e : set) {
                System.out.println(e);
                if(e instanceof VMDeathEvent) {
                    return false;
                }
                else if(e instanceof ClassPrepareEvent) {
                    ReferenceType classReference = ((ClassPrepareEvent) e).referenceType();
                    if(_cachedPoints.containsKey(classReference.name())) {
                        Vector<String> watchpoints = _cachedPoints.get(classReference.name()).getValue();
                        for(String watchpoint : watchpoints)
                            createWatchpointRequest(classReference, watchpoint);
                        Vector<String> breakpoints = _cachedPoints.get(classReference.name()).getKey();
                        for(String breakpoint : breakpoints)
                            createBreakpointRequest(classReference, breakpoint);
                    }
                }
                else if(e instanceof ModificationWatchpointEvent) {
                    _state = PAUSED;
                    ModificationWatchpointEvent mwe = (ModificationWatchpointEvent) e;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG,
                            String.format("Variable %s in %s modified! Old:%s, New:%s",
                                    mwe.field(), mwe.location(), mwe.valueCurrent(), mwe.valueToBe())));
                    _threadRef = mwe.thread();
                    removeStepRequests();
                    analyseVariables(mwe);
                }
                else if(e instanceof BreakpointEvent) {
                    _state = PAUSED;
                    BreakpointEvent be = (BreakpointEvent) e;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG,
                            String.format("Breakpoint hit @%s", be.location())));
                    _threadRef = be.thread();
                    removeStepRequests();
                    analyseVariables(be);
                }
                else if(e instanceof StepEvent) {
                    _threadRef = ((StepEvent) e).thread();
                    _state = PAUSED;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG, "Stepped over a line."));
                    e.request().disable();
                    analyseVariables((LocatableEvent) e);
                }
            }
            if(_state == RUNNING)
                set.resume();
        }
        return true;
    }

    private void analyseVariables(LocatableEvent e) {
        try {
            ThreadReference threadReference = e.thread();
            Location location = e.location();
            ReferenceType referenceType = location.declaringType();
            StackFrame stackFrame = threadReference.frame(0);
            ObjectReference thisObject = stackFrame.thisObject();

            List<LocalVariable> localVariables = stackFrame.visibleVariables();
            Map<LocalVariable, Value> valuesOfLocalVars = stackFrame.getValues(localVariables);
            System.out.println("Local variables:");
            for(Entry<LocalVariable, Value> entry : valuesOfLocalVars.entrySet()) {
                LocalVariable lv = entry.getKey();
                Value v = entry.getValue();
                System.out.println(String.format("%s (%s) = %s", lv.name(), lv.typeName(), v));
            }

            List<Field> allFields = referenceType.fields();
            if(thisObject != null) {
                Map<Field, Value> valuesOfFields = thisObject.getValues(allFields);
                System.out.println("Instance variables:");
                for(Entry<Field, Value> entry : valuesOfFields.entrySet()) {
                    Field f = entry.getKey();
                    Value v = entry.getValue();
                    String typeName = (f.isStatic()) ? "static " + f.typeName() : f.typeName();
                    System.out.println(String.format("%s (%s) = %s", f.name(), typeName, v));
                }
            }
            else {
                List<Field> staticFields = allFields.stream().filter(TypeComponent::isStatic)
                        .collect(Collectors.toCollection(LinkedList::new));

                Map<Field, Value> valuesOfFields = referenceType.getValues(staticFields);
                System.out.println("Static variables:");
                for(Entry<Field, Value> entry : valuesOfFields.entrySet()) {
                    Field f = entry.getKey();
                    Value v = entry.getValue();
                    System.out.println(String.format("%s (%s) = %s", f.name(), f.typeName(), v));

                    if(v instanceof ArrayReference) {
                        ArrayReference var = (ArrayReference) v;
                    }
                    else if(v instanceof ObjectReference) {
                        ObjectReference vor = (ObjectReference) v;
                        ReferenceType refType = vor.referenceType();
                        List<Field> fieldsOfValue = refType.fields();
                        Map<Field, Value> vofov = vor.getValues(fieldsOfValue);
                        for(Entry<Field, Value> vEntry : vofov.entrySet()) {
                            Field vf = vEntry.getKey();
                            Value vv = vEntry.getValue();
                            String typeName = (vf.isStatic()) ? "static " + vf.typeName() : vf.typeName();
                            System.out.println(String.format("\t %s (%s) = %s", vf.name(), typeName, vv));
                        }
                    }
                }
            }
        }
        catch(Exception ex) {
            System.out.println("Exception");
        }
    }

    private void removeStepRequests() {
        _instance.eventRequestManager().stepRequests().stream()
                .filter(sr -> _threadRef.equals(sr.thread()))
                .forEach(EventRequest::disable);
    }

    private void createBreakpointRequest(ReferenceType classReference, String breakpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Location l = classReference.methodsByName(breakpoint).get(0).location();
        BreakpointRequest br = erm.createBreakpointRequest(l);
        br.enable();
    }

    private void createWatchpointRequest(ReferenceType classReference, String watchpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Field f = classReference.fieldByName(watchpoint);
        ModificationWatchpointRequest mwe = erm.createModificationWatchpointRequest(f);
        mwe.enable();
    }

    public void addWatchpoint(String className, String argument) {
        List<ReferenceType> classes = _instance.classesByName(className);
        if(!classes.isEmpty()) {
            createWatchpointRequest(classes.get(0), argument);
        }
        else {
            Vector<String> entries;
            if (_cachedPoints.containsKey(className)) {
                entries = _cachedPoints.get(className).getValue();
            } else {
                createClassPrepareRequest(className);
                entries = new Vector<>();
                _cachedPoints.put(className, new SimpleImmutableEntry<>(new Vector<>(), entries));
            }
            entries.add(argument);
        }
    }

    public void addBreakpoint(String className, String argument) {
        List<ReferenceType> classes = _instance.classesByName(className);
        if(!classes.isEmpty()) {
            createBreakpointRequest(classes.get(0), argument);
        }
        else {
            Vector<String> entries;
            if (_cachedPoints.containsKey(className)) {
                entries = _cachedPoints.get(className).getKey();
            } else {
                createClassPrepareRequest(className);
                entries = new Vector<>();
                _cachedPoints.put(className, new SimpleImmutableEntry<>(entries, new Vector<>()));
            }
            entries.add(argument);
        }
    }

    public void createStepRequest() {
        if(_threadRef != null && _state == PAUSED) {
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadRef, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
            sr.addCountFilter(1);
            sr.enable();
            resume();
            _state = RUNNING;
        }
    }

    private void createClassPrepareRequest(String filter) {
        EventRequestManager erm = _instance.eventRequestManager();
        ClassPrepareRequest cpr = erm.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.enable();
    }
}