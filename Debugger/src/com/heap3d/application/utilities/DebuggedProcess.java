package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.ProcessEvent;
import com.heap3d.application.events.ProcessEventType;
import com.heap3d.application.events.StartDefinition;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        service.submit(new StreamListener(_eventBus, _process.getInputStream()));
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
                            addWatchpoint(classReference, watchpoint);
                        Vector<String> breakpoints = _cachedPoints.get(classReference.name()).getKey();
                        for(String breakpoint : breakpoints)
                            addBreakpoint(classReference, breakpoint);
                    }
                }
                else if(e instanceof ModificationWatchpointEvent) {
                    _state = PAUSED;
                    ModificationWatchpointEvent mwe = (ModificationWatchpointEvent) e;
                    _eventBus.post(new ProcessEvent(ProcessEventType.DEBUG_MSG,
                            String.format("Variable %s in %s modified! Old:%s, New:%s",
                                    mwe.field(), mwe.location(), mwe.valueCurrent(), mwe.valueToBe())));
                }
                else if(e instanceof BreakpointEvent) {
                    _state = PAUSED;
                    BreakpointEvent be = (BreakpointEvent) e;
                    _eventBus.post(new ProcessEvent(ProcessEventType.DEBUG_MSG,
                            String.format("Breakpoint hit @%s", be.location())));
                }
            }
            if(_state == RUNNING)
                set.resume();
        }
        return true;
    }

    private void addBreakpoint(ReferenceType classReference, String breakpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Location l = classReference.methodsByName(breakpoint).get(0).location();
        BreakpointRequest br = erm.createBreakpointRequest(l);
        br.setEnabled(true);
    }

    private void addWatchpoint(ReferenceType classReference, String watchpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Field f = classReference.fieldByName(watchpoint);
        ModificationWatchpointRequest mwe = erm.createModificationWatchpointRequest(f);
        mwe.setEnabled(true);
    }

    public void cacheWatchpointUntilClassIsLoaded(String className, String argument) {
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

    public void cacheBreakpointUntilClassIsLoaded(String className, String argument) {
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

    public void createStepRequest() {
        EventRequestManager erm = _instance.eventRequestManager();
//        erm.createStepRequest()
    }

    private void createClassPrepareRequest(String filter) {
        EventRequestManager erm = _instance.eventRequestManager();
        ClassPrepareRequest cpr = erm.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.setEnabled(true);
    }
}