package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventDTO;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;

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

                }
                set.resume();
            }
        }
    }

    private boolean handleControlQueueItem(EventDTO e) throws IOException {
        System.out.println(e.type);
        switch (e.type) {
            case START: {
                System.out.println("creating @" + _definition.port);
                _process = createProcess();
                _virtualMachineInstance = _virtualMachineProvider.connect(_definition.port);
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