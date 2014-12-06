package com.imperial.heap3d.implementations.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.application.*;
import com.imperial.heap3d.implementations.events.StartDefinition;
import com.imperial.heap3d.implementations.jdi.DVirtualMachine;
import com.imperial.heap3d.implementations.layout.Bridge;
import com.imperial.heap3d.implementations.layout.IRenderEngine;
import com.imperial.heap3d.interfaces.application.IBreakpointManager;
import com.imperial.heap3d.interfaces.application.IStepManager;
import com.imperial.heap3d.interfaces.application.IVariableAnalyser;
import com.imperial.heap3d.interfaces.factories.IVirtualMachineProvider;
import com.imperial.heap3d.interfaces.jdi.IVirtualMachine;
import com.imperial.heap3d.utilities.Check;

/**
 * Created by oskar on 28/11/14.
 */
public class ProcessFactory {

    private IVirtualMachineProvider _vmProvider;
    private EventBus _eventBus;
    private IRenderEngine _renderEngine;

    public ProcessFactory(IVirtualMachineProvider vmProvider, EventBus eventBus, IRenderEngine renderEngine) {
        _vmProvider = Check.NotNull(vmProvider, "vmProvider");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _renderEngine = Check.NotNull(renderEngine, "renderEngine");
    }

    public ControlEventHandler buildComponents(StartDefinition startDefinition) {
        Check.NotNull(startDefinition, "startDefinition");

        int port = getRandomPort();
        EventBus processStateEventBus = new EventBus();
        ConnectedProcess cp = _vmProvider.establish(port, () -> startDefinition.buildProcess(port, _eventBus));
        IVirtualMachine vm = new DVirtualMachine(cp.getVirtualMachine(), processStateEventBus);

        IStepManager _stepManager = new StepManager(vm);
        IBreakpointManager _breakpointManager = new BreakpointManager(vm);

        Bridge bridge = new Bridge(_renderEngine, _eventBus);
        IVariableAnalyser _variableAnalyser = new VariableAnalyser(new NodeBuilder(), bridge);
        DebuggedProcess _debuggedProcess = new DebuggedProcess(cp.getProcess(), _breakpointManager, _stepManager,
                _variableAnalyser, _eventBus, processStateEventBus);

        return new ControlEventHandler(_debuggedProcess, _eventBus, vm,
                _breakpointManager, _stepManager);
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }
}
