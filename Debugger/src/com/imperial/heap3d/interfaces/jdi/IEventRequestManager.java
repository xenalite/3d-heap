package com.imperial.heap3d.interfaces.jdi;

import com.sun.jdi.ThreadReference;

/**
 * Created by om612 on 01/12/14.
 */
public interface IEventRequestManager {

    public void disableAllStepRequests(ThreadReference threadReference);

    public void addStepRequest(ThreadReference threadReference, int size, int depth);
}
