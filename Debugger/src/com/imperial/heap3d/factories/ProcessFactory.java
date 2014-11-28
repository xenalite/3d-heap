package com.imperial.heap3d.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.application.BreakpointManager;
import com.imperial.heap3d.application.ConnectedProcess;
import com.imperial.heap3d.application.ControlEventHandler;
import com.imperial.heap3d.application.DebuggedProcess;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.utilities.Check;
import com.sun.jdi.event.Event;

import static com.imperial.heap3d.application.ProcessState.RUNNING;

/**
 * Created by oskar on 28/11/14.
 */
public class ProcessFactory {

    private static final String EXCEPTION_FORMAT = "Process Factory: %s not build yet.";
    private IVirtualMachineProvider _vmProvider;
    private EventBus _eventBus;
    private HeapGraphFactory _heapGraphFactory;

    private DebuggedProcess _debuggedProcess;
    private ControlEventHandler _controlEventHandler;
    private BreakpointManager _breakpointManager;

    public ProcessFactory(IVirtualMachineProvider vmProvider, EventBus eventBus, HeapGraphFactory heapGraphFactory) {
        _vmProvider = Check.NotNull(vmProvider);
        _eventBus = Check.NotNull(eventBus);
        _heapGraphFactory = Check.NotNull(heapGraphFactory);
    }

    public void buildComponents(StartDefinition startDefinition) {
        Check.NotNull(startDefinition);

        int port = getRandomPort();
        ConnectedProcess cp = _vmProvider.establish(port, () -> startDefinition.buildProcess(port));
        
    }

    private void runProcessAndEstablishConnection() {
        int port = getRandomPort();
        ConnectedProcess cp = _provider.establish(port, () -> _definition.buildProcess(port));
        _instance = cp.virtualMachine;
        _process = cp.process;
        _manager = new BreakpointManager(_instance);
        _state = RUNNING;
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }

    public DebuggedProcess getDebuggedProcess() {
        if(_debuggedProcess == null)
            throw new IllegalStateException(String.format(EXCEPTION_FORMAT, "DebuggedProcess"));
        return _debuggedProcess;
    }

    public ControlEventHandler getControlEventHandler() {
        if(_controlEventHandler == null)
            throw new IllegalStateException(String.format(EXCEPTION_FORMAT, "ControlEventHandler"));
        return _controlEventHandler;
    }

    public BreakpointManager getBreakpointManager() {
        if(_breakpointManager == null)
            throw new IllegalStateException(String.format(EXCEPTION_FORMAT, "BreakpointManager"));
        return _breakpointManager;
    }
}
