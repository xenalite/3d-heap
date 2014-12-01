package com.imperial.heap3d.implementations.layout.animation;

import com.imperial.heap3d.implementations.snapshot.Node;

public class SelectedAnimation extends AnimationEvent{

	private static final float GROW_RANGE = 0.3f;
	
	private float startingScale, bound;
	private float incScale;
	
	public SelectedAnimation(Node selectedNode) {
		ANIMATION_TIME = 30;
		this.shape = selectedNode.getGeometry();
		startingScale = shape.getEntity().getScale();
		bound = (GROW_RANGE*startingScale);
		incScale = bound/ANIMATION_TIME;
	}

	@Override
	public void step(){
		float newScale = shape.getEntity().getScale()+incScale;
		shape.getEntity().setScale(newScale);
		
		if(newScale >= startingScale + bound || newScale <= startingScale - bound)
			incScale = -incScale;
	}
	
	public void stop(){
		System.out.println("here------");
		shape.getEntity().setScale(startingScale);
		System.out.println(startingScale);
		System.out.println(shape.getEntity().getScale());
	}
	
}
