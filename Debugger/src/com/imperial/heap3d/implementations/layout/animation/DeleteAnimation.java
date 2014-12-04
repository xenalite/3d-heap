package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.entities.Entity;
import com.imperial.heap3d.implementations.snapshot.Node;

public class DeleteAnimation extends AnimationEvent {

	private Entity entity;
	private float decrementScale;

	public DeleteAnimation(Node nodeToDelete) {
		entity = nodeToDelete.getGeometry().getEntity();
		decrementScale = -nodeToDelete.getGeometry().getEntity().getScale() / maxIterations;
	}

	@Override
	public void executeStep() {
		float newScale = entity.getScale() + decrementScale;
		entity.setScale(newScale < 0 ? 0 : newScale);
	}

	@Override
	public void finish() {
		entity.setScale(0);
	}
}
