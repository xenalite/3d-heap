package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.MyEvent;

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
}
