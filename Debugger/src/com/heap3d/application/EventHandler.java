package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.ControlEvent;
import com.heap3d.application.events.ProcessEvent;
import com.heap3d.application.events.ProcessEventType;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.DebuggedProcess;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.heap3d.application.utilities.ProcessState;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.heap3d.application.utilities.ProcessState.*;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Map.Entry;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private DebuggedProcess _dprocess;
    private StartDefinition _definition;
    private Process _process;
    private ProcessState _state;
    private EventBus _eventBus;
    private VirtualMachine _instance;
    private IVirtualMachineProvider _provider;
    private ConcurrentLinkedDeque<ControlEvent> _controlEventQueue;
    private Map<String, Entry<Vector<String>, Vector<String>>> _cachedBWPoints;

    public EventHandler(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _dprocess = new DebuggedProcess(definition, provider, eventBus);
        _definition = definition;
        _state = ProcessState.STOPPED;
        _provider = provider;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _cachedBWPoints = new HashMap<>();
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        _controlEventQueue.add(e);
    }

    public void loop() throws InterruptedException, IOException {
        while (true) {

            while (!_controlEventQueue.isEmpty())
                if(!handleControlQueueItem(_controlEventQueue.removeFirst()))
                    return;

            _dprocess.waitForEvents();
            if(_instance != null && _state == RUNNING) {
                EventQueue vmQueue = _instance.eventQueue();
                EventSet set = vmQueue.remove();
                for(Event e : set) {
                    System.out.println(e);
                    if(e instanceof VMDeathEvent) {
                        return;
                    }
                    else if(e instanceof ClassPrepareEvent) {
                      ReferenceType classReference = ((ClassPrepareEvent) e).referenceType();
                        if(_cachedBWPoints.containsKey(classReference.name())) {
                            Vector<String> watchpoints = _cachedBWPoints.get(classReference.name()).getValue();
                            for(String watchpoint : watchpoints)
                                addWatchpoint(classReference, watchpoint);
                            Vector<String> breakpoints = _cachedBWPoints.get(classReference.name()).getKey();
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
        }
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

    private boolean handleControlQueueItem(ControlEvent e) throws IOException, InterruptedException {
        switch (e.type) {
            case START: {
                _dprocess.start();
            }
            break;
            case STOP: {
                return false;
            }
            case PAUSE: {
                _dprocess.pause();
                if (_instance != null && _state == RUNNING) {
                    _state = PAUSED;
                    _instance.suspend();
                }
            }
            break;
            case RESUME: {
                _dprocess.resume();
                if (_instance != null && _state == PAUSED) {
                    _state = RUNNING;
                    _instance.resume();
                }
            }
            break;
            case STEP: {
                if(_instance != null && _state == PAUSED) {
                    createStepRequest();
                }
            }
            break;
            case BREAKPOINT: {
                cacheBreakpointUntilClassIsLoaded(e);
            }
            break;
            case WATCHPOINT: {
                cacheWatchpointUntilClassIsLoaded(e);
            }
            break;
        }
        return true;
    }

    private void createStepRequest() {
        EventRequestManager erm = _instance.eventRequestManager();
//        erm.createStepRequest()
    }



    private void cacheWatchpointUntilClassIsLoaded(ControlEvent e) {
        Vector<String> entries;
        if (_cachedBWPoints.containsKey(e.className)) {
            entries = _cachedBWPoints.get(e.className).getValue();
        } else {
            createClassPrepareRequest(e.className);
            entries = new Vector<>();
            _cachedBWPoints.put(e.className, new SimpleImmutableEntry<>(new Vector<>(), entries));
        }
        entries.add(e.argument);
    }

    private void cacheBreakpointUntilClassIsLoaded(ControlEvent e) {
        Vector<String> entries;
        if (_cachedBWPoints.containsKey(e.className)) {
            entries = _cachedBWPoints.get(e.className).getKey();
        } else {
            createClassPrepareRequest(e.className);
            entries = new Vector<>();
            _cachedBWPoints.put(e.className, new SimpleImmutableEntry<>(entries, new Vector<>()));
        }
        entries.add(e.argument);
    }

    private void createClassPrepareRequest(String filter) {
        EventRequestManager erm = _instance.eventRequestManager();
        ClassPrepareRequest cpr = erm.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.setEnabled(true);
    }

    public void run() throws InterruptedException, IOException {
        loop();
        dispose();
    }

    private void dispose() {
        _dprocess.dispose();
        _state = STOPPED;
        _process.destroy();
        _eventBus.post(new ProcessEvent(ProcessEventType.STOPPED));
        _eventBus.unregister(this);
    }
}