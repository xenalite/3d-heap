package com.heap3d.interfaces.jdi;

import com.heap3d.implementations.application.ProcessState;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.event.EventQueue;

import java.util.List;

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

    public List<ReferenceType> classesByName(String className);
}
