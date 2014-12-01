package com.imperial.heap3d.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.application.*;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.utilities.Check;

import java.util.concurrent.ExecutorService;

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
        _vmProvider = Check.NotNull(vmProvider, "vmProvider");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _heapGraphFactory = Check.NotNull(heapGraphFactory, "heapGraphFactory");
    }

    public void buildComponents(StartDefinition startDefinition) {
        Check.NotNull(startDefinition, "startDefinition");

        int port = getRandomPort();
        ConnectedProcess cp = _vmProvider.establish(port, () -> startDefinition.buildProcess(port));
        _breakpointManager = new BreakpointManager(cp.getVirtualMachine());
        _debuggedProcess = new DebuggedProcess(cp, _breakpointManager, _heapGraphFactory, _eventBus);
        _controlEventHandler = new ControlEventHandler(_debuggedProcess, _eventBus, _breakpointManager);
        Process p = cp.getProcess();

        ExecutorService service = ThreadBuilder.createService("stream-listener");
        service.submit(new StreamListener(_eventBus, p.getInputStream(), p.getErrorStream()));
        service.shutdown();
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
