package org.debugger.application.utilities;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private EventBus _eventBus;
    private IVirtualMachineProvider _virtualMachineProvider;

    public EventHandler(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    @Subscribe
    public void handleStartVirtualMachine(int port) {
        _virtualMachineProvider.createAtPort(port);
    }

    public void runEventLoop() {
        while(true) {
            System.out.println("outer");
            while(_virtualMachineProvider.getVirtualMachine() == null) {
                try {
                    System.out.println("waiting for vm");
                    wait();
                } catch (InterruptedException ignored) { ignored.printStackTrace(); }
            }
        }
    }
}
