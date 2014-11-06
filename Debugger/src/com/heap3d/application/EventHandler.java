package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.Event;
import com.heap3d.application.events.EventType;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.VirtualMachine;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.heap3d.application.events.EventType.START;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private Process _currentProcess;
    private EventBus _eventBus;
    private VirtualMachine _virtualMachineInstance;
    private IVirtualMachineProvider _virtualMachineProvider;
    private ConcurrentLinkedDeque<Event> _controlEventQueue;

    public EventHandler(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(Event e) {
        _controlEventQueue.add(e);
    }

    public void loop() throws InterruptedException, IOException {
        boolean stop = false;
        while (!stop) {

            while (!_controlEventQueue.isEmpty()) {
                Event e = _controlEventQueue.removeFirst();
                switch (e.type) {
                    case START: {

                        _currentProcess = createProcess(e);
                        _virtualMachineInstance = _virtualMachineProvider.connect(Integer.valueOf(e.argument));
                    }
                    break;
                    case STOP: {
                        return;
                    }
                    case PAUSE: {
                        if (_virtualMachineInstance != null) {
                            _virtualMachineInstance.suspend();
                        }
                    }
                    break;
                    case RESUME: {
                        if (_virtualMachineInstance != null) {
                            _virtualMachineInstance.resume();
                        }
                    }
                    break;
                    case BREAKPOINT: {

                    }
                    break;
                    case WATCHPOINT: {

                    }
                    break;
                }
            }
            System.out.println("Tick");
            Thread.sleep(1000);
        }
    }

    private Process createProcess(Event e) throws IOException {
        String command = e.argument + " " + e.className;
        System.out.println(command);
        return Runtime.getRuntime().exec(command);
    }

    public void run() throws InterruptedException, IOException {
        loop();
        dispose();
        System.out.println("Exit");
    }

    private void dispose() {
        _virtualMachineInstance.exit(0);
        _virtualMachineInstance = null;
        _currentProcess.destroy();
        _eventBus.unregister(this);
    }
}