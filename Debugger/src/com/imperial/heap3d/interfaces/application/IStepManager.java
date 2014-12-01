package com.imperial.heap3d.interfaces.application;

import com.sun.jdi.ThreadReference;

/**
 * Created by om612 on 01/12/14.
 */
public interface IStepManager {

    public void notifyPausedAtLocation(ThreadReference threadReference);

    public void createStepOverRequest();

    public void createStepIntoRequest();

    public void createStepOutRequest();
}