package com.heap3d.interfaces.animation;

/**
 * Created by oskar on 04/12/14.
 */
public interface IAnimation {

    public boolean executeStepAndCheckIfDone();

    public void finalise();
    
    public boolean hasAnythingChanged();
}
