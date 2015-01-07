package com.heap3d.interfaces.application;

import com.heap3d.implementations.application.ProcessState;
import com.sun.jdi.event.EventQueue;

/**
 * Created by om612 on 01/12/14.
 */
public interface IDebuggedProcess {

    public boolean handleEvents(EventQueue eventQueue);

    public void dispose();

    public void notifyStateChanged(ProcessState newState);
}