package com.imperial.heap3d.interfaces.application;

import com.sun.jdi.event.LocatableEvent;

/**
 * Created by om612 on 01/12/14.
 */
public interface IStepManager {

    public void notifyPausedAtLocation(LocatableEvent event);

    public void createStepOverRequest();

    public void createStepIntoRequest();

    public void createStepOutRequest();
}