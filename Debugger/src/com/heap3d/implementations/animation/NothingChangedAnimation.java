package com.heap3d.implementations.animation;

import com.heap3d.interfaces.animation.IAnimation;

/**
 * Created by oskar on 04/12/14.
 */
public class NothingChangedAnimation implements IAnimation {
	
	private boolean onlyOnce = true;
	
    @Override
    public boolean executeStepAndCheckIfDone() {
    	if(onlyOnce){
    		onlyOnce = false;
    		return true;
    	}
        return false;
    }

    @Override
    public void finalise() {}

	@Override
	public boolean hasAnythingChanged() {
		return false;
	}
}
