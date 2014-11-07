package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventDTO;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.ConnectedProcess;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.Callable;
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
                        ClassPrepareEvent cpe = (ClassPrepareEvent) e;
                        System.out.println(((ClassPrepareEvent) e).referenceType());
                    }
                }
                set.resume();
            }
        }
    }

    public int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
//        return 10000;
    }

    private boolean handleControlQueueItem(EventDTO e) throws IOException, InterruptedException {
        System.out.println(e.type);
        switch (e.type) {
            case START: {
                int port = getRandomPort();
                System.out.println("creating @" + port);

                ConnectedProcess cp = _virtualMachineProvider.establish(port, () -> createProcess(port));
                _virtualMachineInstance = cp.virtualMachine;
                _process = cp.process;
                System.out.println("attached @" + port);

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

        MethodEntryRequest mer = erm.createMethodEntryRequest();
        mer.addClassFilter(_definition.className);
        mer.setEnabled(true);
    }

    private Process createProcess(int port) throws IOException {
//        String command = _definition.command + _definition.className;
//        return Runtime.getRuntime().exec(command);
        return _definition.buildProcess(port);
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