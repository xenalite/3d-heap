package com.graphics.movement;

import com.graphics.entities.Camera;

public abstract class Input {
	
	public abstract void move(Camera camera);
	
	public boolean isEnabled(){
		return true;
	}
	
	public void cleanup(){}
}
