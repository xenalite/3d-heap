package com.heap3d.implementations.layout.animation;

import com.graphics.entities.Entity;
import com.graphics.shapes.Shape;

public class DeleteAnimation extends AnimationEvent {

	private Entity entity;
	private float decrementScale;

	public DeleteAnimation(Shape shapeToDelete) {
		entity = shapeToDelete.getEntity();
		decrementScale = -shapeToDelete.getEntity().getScale() / maxIterations;
	}

	@Override
	public void step() {
		float newScale = entity.getScale() + decrementScale;
		entity.setScale(newScale < 0 ? 0 : newScale);
		++iteration;
	}

	@Override
	public void finish() {
		entity.setScale(0);
	}
}
