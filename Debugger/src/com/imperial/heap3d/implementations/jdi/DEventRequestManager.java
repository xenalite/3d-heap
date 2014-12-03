package com.imperial.heap3d.implementations.jdi;

import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.jdi.IEventRequestManager;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.*;

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
        ThreadReference reference = Check.NotNull(threadReference, "threadReference");
        List<StepRequest> stepRequests = _jdiEventRequestManager.stepRequests();
        stepRequests.stream().filter(sr -> sr.thread().equals(reference)).forEach(EventRequest::disable);
    }

    @Override
    public void createStepRequest(ThreadReference threadReference, int size, int depth) {
        ThreadReference reference = Check.NotNull(threadReference, "threadReference");
        StepRequest stepRequest = _jdiEventRequestManager.createStepRequest(reference, size, depth);
        stepRequest.addCountFilter(1);
        stepRequest.enable();
    }

    @Override
    public void createClassPrepareRequest(String filter) {
        filter = Check.NotNull(filter, "filter");
        ClassPrepareRequest cpr = _jdiEventRequestManager.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.enable();
    }

    @Override
    public void deleteEventRequest(EventRequest eventRequest) {
        eventRequest = Check.NotNull(eventRequest, "eventRequest");
        _jdiEventRequestManager.deleteEventRequest(eventRequest);
    }

    @Override
    public BreakpointRequest createBreakpointRequest(ReferenceType referenceType, String method) {
        referenceType = Check.NotNull(referenceType, "referenceType");
        method = Check.NotNull(method, "method");

        Location l = referenceType.methodsByName(method).get(0).location();
        BreakpointRequest br = _jdiEventRequestManager.createBreakpointRequest(l);
        br.enable();
        return br;
    }

    @Override
    public ModificationWatchpointRequest
        createModificationWatchpointRequest(ReferenceType referenceType, String field) {
        referenceType = Check.NotNull(referenceType, "referenceType");
        field = Check.NotNull(field, "field");

        Field f = referenceType.fieldByName(field);
        ModificationWatchpointRequest mwr = _jdiEventRequestManager.createModificationWatchpointRequest(f);
        mwr.enable();
        return mwr;
    }
}
