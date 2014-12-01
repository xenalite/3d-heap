package com.imperial.heap3d.implementations.application;

import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.application.IStepManager;
import com.imperial.heap3d.interfaces.jdi.IEventRequestManager;
import com.imperial.heap3d.interfaces.jdi.IVirtualMachine;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.StepRequest;

/**
 * Created by om612 on 01/12/14.
 */
public class StepManager implements IStepManager {

    private final IVirtualMachine _virtualMachine;
    private ThreadReference _threadReference;

    public StepManager(IVirtualMachine virtualMachine) {
        _virtualMachine = Check.NotNull(virtualMachine, "virtualMachine");
    }

    @Override
    public void createStepOverRequest() {
        createStepRequest(StepRequest.STEP_LINE, StepRequest.STEP_OVER);
    }

    @Override
    public void createStepIntoRequest() {
        createStepRequest(StepRequest.STEP_MIN, StepRequest.STEP_INTO);
    }

    @Override
    public void createStepOutRequest() {
        createStepRequest(StepRequest.STEP_LINE, StepRequest.STEP_OUT);
    }

    private void createStepRequest(int size, int depth) {
        if (_threadReference != null) {
            IEventRequestManager eventRequestManager = _virtualMachine.getEventRequestManager();
            eventRequestManager.disableAllStepRequests(_threadReference);
            eventRequestManager.addStepRequest(_threadReference, size, depth);

            _virtualMachine.resume();
        }
    }

    @Override
    public void notifyPausedAtLocation(ThreadReference threadReference) {
        _threadReference = threadReference;
    }
}
