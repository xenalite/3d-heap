package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.entities.Entity;
import com.imperial.heap3d.implementations.snapshot.Node;

public class AddAnimation extends AnimationEvent {

	private float increment;
	private float finalScale;
	private Entity entity;

	public AddAnimation(Node newNode) {
		entity = newNode.getGeometry().getEntity();
		finalScale = newNode.getGeometry().getEntity().getScale();
		increment = finalScale / maxIterations;
		entity.setScale(0);
	}

	@Override
	protected void executeStep() {
		entity.setScale(entity.getScale() + increment);
	}

	@Override
	public void finish() {
		entity.setScale(finalScale);
	}
}
