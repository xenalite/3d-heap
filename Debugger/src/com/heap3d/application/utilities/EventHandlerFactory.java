package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.EventHandler;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.IVirtualMachineProvider;

/**
 * Created by oskar on 01/11/14.
 */
public class EventHandlerFactory {

    private final IVirtualMachineProvider _virtualMachineProvider;
    private final EventBus _eventBus;

    public EventHandlerFactory(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
    }

    public EventHandler create(StartDefinition sd) {
        return new EventHandler(sd, _virtualMachineProvider, _eventBus);
    }
}
