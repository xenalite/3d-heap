package com.imperial.heap3d.implementations.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.application.*;
import com.imperial.heap3d.implementations.events.StartDefinition;
import com.imperial.heap3d.implementations.jdi.DVirtualMachine;
import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.application.IBreakpointManager;
import com.imperial.heap3d.interfaces.application.IStepManager;
import com.imperial.heap3d.interfaces.application.IVariableAnalyser;
import com.imperial.heap3d.interfaces.factories.IVirtualMachineProvider;
import com.imperial.heap3d.interfaces.jdi.IVirtualMachine;

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
    private IBreakpointManager _breakpointManager;
    private IStepManager _stepManager;
    private IVariableAnalyser _variableAnalyser;

    public ProcessFactory(IVirtualMachineProvider vmProvider, EventBus eventBus, HeapGraphFactory heapGraphFactory) {
        _vmProvider = Check.NotNull(vmProvider, "vmProvider");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _heapGraphFactory = Check.NotNull(heapGraphFactory, "heapGraphFactory");
    }

    public ControlEventHandler buildComponents(StartDefinition startDefinition) {
        Check.NotNull(startDefinition, "startDefinition");

        int port = getRandomPort();
        EventBus processStateEventBus = new EventBus();
        ConnectedProcess cp = _vmProvider.establish(port, () -> startDefinition.buildProcess(port));
        Process p = cp.getProcess();
        IVirtualMachine vm = new DVirtualMachine(cp.getVirtualMachine(), processStateEventBus);

        _stepManager = new StepManager(vm);
        _breakpointManager = new BreakpointManager(cp.getVirtualMachine());

        _variableAnalyser = new VariableAnalyser(new NodeBuilder(), _heapGraphFactory);
        _debuggedProcess = new DebuggedProcess(cp.getProcess(), _breakpointManager, _stepManager,
                _variableAnalyser, _eventBus, processStateEventBus);

        _controlEventHandler = new ControlEventHandler(_debuggedProcess, _eventBus, vm,
                _breakpointManager, _stepManager);

        ExecutorService service = ThreadBuilder.createService("stream-listener");
        service.submit(new StreamListener(_eventBus, p.getInputStream(), p.getErrorStream()));
        service.shutdown();

        return _controlEventHandler;
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }
}
