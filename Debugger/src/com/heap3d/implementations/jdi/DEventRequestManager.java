package com.heap3d.implementations.jdi;

import com.heap3d.utilities.Check;
import com.heap3d.interfaces.jdi.IEventRequestManager;
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
        _jdiEventRequestManager = Check.notNull(eventRequestManager, "eventRequestManager");
    }

    @Override
    public void disableAllStepRequests(ThreadReference threadReference) {
        ThreadReference reference = Check.notNull(threadReference, "threadReference");
        List<StepRequest> stepRequests = _jdiEventRequestManager.stepRequests();
        stepRequests.stream().filter(sr -> sr.thread().equals(reference)).forEach(EventRequest::disable);
    }

    @Override
    public void createStepRequest(ThreadReference threadReference, int size, int depth) {
        ThreadReference reference = Check.notNull(threadReference, "threadReference");
        StepRequest stepRequest = _jdiEventRequestManager.createStepRequest(reference, size, depth);
        stepRequest.addCountFilter(1);
        stepRequest.addClassExclusionFilter("java.*");
        stepRequest.enable();
    }

    @Override
    public void createClassPrepareRequest(String filter) {
        filter = Check.notNull(filter, "filter");
        ClassPrepareRequest cpr = _jdiEventRequestManager.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.enable();
    }

    @Override
    public void deleteEventRequest(EventRequest eventRequest) {
        eventRequest = Check.notNull(eventRequest, "eventRequest");
        _jdiEventRequestManager.deleteEventRequest(eventRequest);
    }

    @Override
    public BreakpointRequest createBreakpointRequest(ReferenceType referenceType, String method) {
        referenceType = Check.notNull(referenceType, "referenceType");
        method = Check.notNull(method, "method");

        Location l = referenceType.methodsByName(method).get(0).location();
        BreakpointRequest br = _jdiEventRequestManager.createBreakpointRequest(l);
        br.enable();
        return br;
    }

    @Override
    public ModificationWatchpointRequest
        createModificationWatchpointRequest(ReferenceType referenceType, String field) {
        referenceType = Check.notNull(referenceType, "referenceType");
        field = Check.notNull(field, "field");

        Field f = referenceType.fieldByName(field);
        ModificationWatchpointRequest mwr = _jdiEventRequestManager.createModificationWatchpointRequest(f);
        mwr.enable();
        return mwr;
    }
}
