package com.imperial.heap3d.interfaces.jdi;

import com.imperial.heap3d.implementations.application.ProcessState;
import com.sun.jdi.event.EventQueue;

/**
 * Created by om612 on 01/12/14.
 */
public interface IVirtualMachine {

    public void resume();

    public void pause();

    public IEventSet waitFor(long timeout);

    public void notifyStateChange(ProcessState newState);

    public IEventRequestManager getEventRequestManager();

    public EventQueue getEventQueue();
}
