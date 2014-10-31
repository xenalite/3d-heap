package org.debugger.application.utilities;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDeathEvent;

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

    public void runEventLoop(int port) {
        _virtualMachineProvider.createAtPort(port);
        VirtualMachine vm = _virtualMachineProvider.getVirtualMachine();
        EventQueue queue = vm.eventQueue();
        while(true) {
            System.out.println("outer");

            EventSet set = null;
            try {
                set = queue.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Event e : set) {
                System.out.println(e);
               if(e instanceof VMDeathEvent) {
                   return;
               }
            }
        }
    }
}
