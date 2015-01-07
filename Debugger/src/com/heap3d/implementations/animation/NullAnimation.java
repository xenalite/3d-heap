package com.heap3d.implementations.animation;

import com.heap3d.interfaces.animation.IAnimation;

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
