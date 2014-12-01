package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.shapes.Shape;

public abstract class AnimationEvent {

	protected static final float ANIMATION_TIME = 120;
	protected Shape shape;
	protected int itteration = 0;

	public abstract void step();
	
	public boolean finishedAnimation() {
		return itteration >= ANIMATION_TIME;
	}
}