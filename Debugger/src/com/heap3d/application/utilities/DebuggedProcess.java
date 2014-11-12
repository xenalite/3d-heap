package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.StartDefinition;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heap3d.application.utilities.ProcessState.RUNNING;

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
        _eventBus.register(this);
    }

    public void dispose() {

    }

    public void start() {
        runProcessAndEstablishConnection();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new StreamListener(_eventBus, _process.getInputStream()));
        service.shutdown();
    }

    private void runProcessAndEstablishConnection() {
        int port = getRandomPort();
        DProcess cp = _provider.establish(port, () -> _definition.buildProcess(port));
        _instance = cp.virtualMachine;
        _process = cp.process;
        _state = RUNNING;
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }

    public void pause() {

    }

    public void resume() {

    }

    public void waitForEvents() {

    }


}
