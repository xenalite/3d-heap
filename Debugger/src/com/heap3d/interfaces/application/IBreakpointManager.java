package com.heap3d.interfaces.application;

import com.sun.jdi.ReferenceType;

/**
 * Created by om612 on 01/12/14.
 */
public interface IBreakpointManager {

    public void notifyClassLoaded(ReferenceType referenceType);

    public void addBreakpoint(String className, String argument);

    public void removeBreakpoint(String className, String argument);

    public void addWatchpoint(String className, String argument);

    public void removeWatchpoint(String className, String argument);
}
