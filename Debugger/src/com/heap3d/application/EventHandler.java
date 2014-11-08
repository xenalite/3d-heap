package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventDTO;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.ConnectedProcess;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.Field;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private StartDefinition _definition;
    private Process _process;
    private EventBus _eventBus;
    private VirtualMachine _virtualMachineInstance;
    private IVirtualMachineProvider _virtualMachineProvider;
    private ConcurrentLinkedDeque<EventDTO> _controlEventQueue;
    private Map<String,List<String>> _cachedWatchpoints;

    public EventHandler(StartDefinition definition, IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _definition = definition;
        _virtualMachineProvider = virtualMachineProvider;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _cachedWatchpoints = new HashMap<>();
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(EventDTO e) {
        _controlEventQueue.add(e);
    }

    public void loop() throws InterruptedException, IOException {
        boolean stop = false;
        while (!stop) {

            while (!_controlEventQueue.isEmpty())
                if(!handleControlQueueItem(_controlEventQueue.removeFirst()))
                    return;

            if(_virtualMachineInstance != null) {
                EventQueue vmQueue = _virtualMachineInstance.eventQueue();
                EventSet set = vmQueue.remove();
                for(Event e : set) {
                    System.out.println(e);
                    if(e instanceof VMDeathEvent) {
                        return;
                    }
                    else if(e instanceof ClassPrepareEvent) {
                      ReferenceType rtype = ((ClassPrepareEvent) e).referenceType();
                        if(_cachedWatchpoints.containsKey(rtype.name())) {
                            List<String> watchpoints = _cachedWatchpoints.get(rtype.name());
                            for(String watchpoint : watchpoints) {
                                addWatchpoint(rtype, watchpoint);
                            }
                        }

                    }
                }
                set.resume();
            }
        }
    }

    private void addWatchpoint(ReferenceType rtype, String watchpoint) {
        EventRequestManager erm = _virtualMachineInstance.eventRequestManager();
        Field f = rtype.fieldByName(watchpoint);
        ModificationWatchpointRequest mwe = erm.createModificationWatchpointRequest(f);
        mwe.setEnabled(true);
    }

    public int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }

    private boolean handleControlQueueItem(EventDTO e) throws IOException, InterruptedException {
        System.out.println(e.type);
        switch (e.type) {
            case START: {
                int port = getRandomPort();
                ConnectedProcess cp = _virtualMachineProvider.establish(port, () -> _definition.buildProcess(port));
                _virtualMachineInstance = cp.virtualMachine;
                _process = cp.process;
            }
            break;
            case STOP: {
                return false;
            }
            case PAUSE: {
                if (_virtualMachineInstance != null)
                    _virtualMachineInstance.suspend();
            }
            break;
            case RESUME: {
                if (_virtualMachineInstance != null)
                    _virtualMachineInstance.resume();
            }
            break;
            case BREAKPOINT: {

            }
            break;
            case WATCHPOINT: {
                handleWatchpoint(e);
            }
            break;
        }
        return true;
    }

    private void handleWatchpoint(EventDTO e) {
        List<String> watchpoints;
        if(_cachedWatchpoints.containsKey(e.className)) {
            watchpoints = _cachedWatchpoints.get(e.className);
        }
        else {
            EventRequestManager erm = _virtualMachineInstance.eventRequestManager();
            ClassPrepareRequest cpr = erm.createClassPrepareRequest();
            cpr.addClassFilter(e.className);
            cpr.setEnabled(true);

           watchpoints = new LinkedList<>();
            _cachedWatchpoints.put(e.className, watchpoints);
        }
        watchpoints.add(e.argument);
    }

    public void run() throws InterruptedException, IOException {
        loop();
        dispose();
    }

    private void dispose() {
        _process.destroy();
        _virtualMachineInstance.exit(0);
        _eventBus.unregister(this);
    }
}