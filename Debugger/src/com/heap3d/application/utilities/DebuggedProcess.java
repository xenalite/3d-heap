package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.StartDefinition;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

/**
 * Created by oskar on 11/11/14.
 */
public class DebuggedProcess {

    private StartDefinition _definition;
    private IVirtualMachineProvider _provider;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private EventBus _eventBus;
    private ThreadReference _threadRef;

    public DebuggedProcess(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _definition = definition;
        _provider = provider;
        _eventBus = eventBus;
    }
}
