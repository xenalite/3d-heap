package com.graphics.userinput;

import com.graphics.entities.Camera;

public abstract class Input {
	
	public abstract void move(Camera camera);
	
	public boolean isEnabled(){
		return true;
	}
	
	public void cleanup(){}
	
	public abstract boolean hasStarted();
	public abstract boolean hasStopped();
	public abstract boolean hasPaused();
	public abstract boolean hasResumed();
	public abstract boolean hasStepedOver();
	public abstract boolean hasStepedInto();
	public abstract boolean hasStepedOut();
	public abstract boolean hasExited();
	public abstract boolean hasReset();
}
