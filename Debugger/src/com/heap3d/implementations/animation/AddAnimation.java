package com.heap3d.implementations.animation;

import com.graphics.entities.Entity;
import com.graphics.shapes.Shape;

public class AddAnimation extends AnimationEvent {

	private float increment;
	private float finalScale;
	private Entity entity;

	public AddAnimation(Shape newShape) {
		entity = newShape.getEntity();
		finalScale = entity.getScale();
		increment = finalScale / maxIterations;
		entity.setScale(0);
	}

	@Override
	public void step() {
		entity.setScale(entity.getScale() + increment);
		++iteration;
	}

	@Override
	public void finish() {
		entity.setScale(finalScale);
	}
}
