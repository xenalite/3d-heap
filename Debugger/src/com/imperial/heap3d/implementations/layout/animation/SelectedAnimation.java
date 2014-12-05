package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.entities.Entity;
import com.imperial.heap3d.implementations.snapshot.Node;

public class SelectedAnimation extends AnimationEvent {

	private static final float GROW_RANGE = 0.2f;

	private float startingScale, incrementScale, bound;
	private Entity entity;

	public SelectedAnimation(Node selectedNode) {
		super(30);
		entity = selectedNode.getGeometry().getEntity();
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
