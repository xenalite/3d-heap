package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventDTO;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.ConnectedProcess;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.IOException;
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

    public EventHandler(StartDefinition definition, IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _definition = definition;
        _virtualMachineProvider = virtualMachineProvider;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
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
                    else if(e instanceof VMStartEvent) {
//                        requestBreakpoint();
                    }
                    else if(e instanceof ClassPrepareEvent) {
                        System.out.println(((ClassPrepareEvent) e).referenceType());
                    }
                }
                set.resume();
            }
        }
    }

    private boolean handleControlQueueItem(EventDTO e) throws IOException, InterruptedException {
        System.out.println(e.type);
        switch (e.type) {
            case START: {
                System.out.println("creating @" + _definition.port);

                ConnectedProcess cp = _virtualMachineProvider.establish(_definition.port, this::createProcess);
                _virtualMachineInstance = cp.virtualMachine;
                _process = cp.process;
                System.out.println("attached @" + _definition.port);
                requestBreakpoint();
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

            }
            break;
        }
        return true;
    }

    private void requestBreakpoint() {
        EventRequestManager erm = _virtualMachineInstance.eventRequestManager();
        ClassPrepareRequest cpr = erm.createClassPrepareRequest();
        cpr.addClassFilter(_definition.className);
        cpr.setEnabled(true);
    }

    private Process createProcess() throws IOException {
        String command = _definition.command + _definition.className;
        return Runtime.getRuntime().exec(command);
    }

    public void run() throws InterruptedException, IOException {
        loop();
        dispose();
    }

    private void dispose() {
        _virtualMachineInstance.exit(0);
        _virtualMachineInstance = null;
        _process.destroy();
        _eventBus.unregister(this);
    }
}