package com.imperial.heap3d.implementations.layout.animation;

/**
 * Created by oskar on 04/12/14.
 */
public class NullAnimation implements IAnimation {
    @Override
    public boolean executeStepAndCheckIfDone() {
        return false;
    }

    @Override
    public void finalise() {}
}
