package com.heap3d.implementations.layout.animation;

import com.graphics.entities.Entity;
import com.graphics.shapes.Shape;

public class SelectedAnimation extends AnimationEvent {

	private static final float GROW_RANGE = 0.2f;

	private float startingScale, incrementScale, bound;
	private Entity entity;

	public SelectedAnimation(Shape shape) {
		super(30);
		entity = shape.getEntity();
		startingScale = entity.getScale();
		bound = (GROW_RANGE * startingScale);
		incrementScale = bound / maxIterations;
	}

	@Override
	public void step() {
		float newScale = entity.getScale() + incrementScale;
		entity.setScale(newScale);

		if (newScale >= startingScale + bound || newScale <= startingScale - bound)
			incrementScale = -incrementScale;
	}

	@Override
	public void finish() {
		entity.setScale(startingScale);
	}
}
