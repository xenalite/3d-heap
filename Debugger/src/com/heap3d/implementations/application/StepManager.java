package com.heap3d.implementations.application;

import com.heap3d.utilities.Check;
import com.heap3d.interfaces.application.IStepManager;
import com.heap3d.interfaces.jdi.IEventRequestManager;
import com.heap3d.interfaces.jdi.IVirtualMachine;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;

/**
 * Created by om612 on 01/12/14.
 */
public class StepManager implements IStepManager {

    private final IVirtualMachine _virtualMachine;
    private ThreadReference _threadReference;

    public StepManager(IVirtualMachine virtualMachine) {
        _virtualMachine = Check.notNull(virtualMachine, "virtualMachine");
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
            eventRequestManager.createStepRequest(_threadReference, size, depth);

            _virtualMachine.resume();
        }
    }

    @Override
    public void notifyPausedAtLocation(LocatableEvent event) {
        event = Check.notNull(event, "event");
        _threadReference = event.thread();
    }
}
