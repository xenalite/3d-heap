package com.imperial.heap3d.implementations.jdi;

import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.jdi.IEventRequestManager;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

import java.util.List;

/**
 * Created by om612 on 01/12/14.
 */
public class DEventRequestManager implements IEventRequestManager {

    private final EventRequestManager _jdiEventRequestManager;

    public DEventRequestManager(EventRequestManager eventRequestManager) {
        _jdiEventRequestManager = Check.NotNull(eventRequestManager, "eventRequestManager");
    }

    @Override
    public void disableAllStepRequests(ThreadReference threadReference) {
        // TODO -- store requests somewhere, then pass a request to delete instead of the thread reference
        ThreadReference reference = Check.NotNull(threadReference, "threadReference");
        List<StepRequest> stepRequests = _jdiEventRequestManager.stepRequests();
        stepRequests.stream().filter(sr -> sr.thread().equals(reference)).forEach(EventRequest::disable);
    }

    @Override
    public void addStepRequest(ThreadReference threadReference, int size, int depth) {
        ThreadReference reference = Check.NotNull(threadReference, "threadReference");
        StepRequest stepRequest = _jdiEventRequestManager.createStepRequest(reference, size, depth);
        stepRequest.addCountFilter(1);
        stepRequest.enable();
    }
}
