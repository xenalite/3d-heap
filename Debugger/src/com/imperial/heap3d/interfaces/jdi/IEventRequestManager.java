package com.imperial.heap3d.interfaces.jdi;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;

/**
 * Created by om612 on 01/12/14.
 */
public interface IEventRequestManager {

    public void disableAllStepRequests(ThreadReference threadReference);

    public void createStepRequest(ThreadReference threadReference, int size, int depth);

    public void createClassPrepareRequest(String filter);

    public void deleteEventRequest(EventRequest eventRequest);

    public BreakpointRequest createBreakpointRequest(ReferenceType referenceType, String method);

    public ModificationWatchpointRequest
        createModificationWatchpointRequest(ReferenceType referenceType, String field);
}
