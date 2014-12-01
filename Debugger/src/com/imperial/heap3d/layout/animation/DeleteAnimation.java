package com.imperial.heap3d.layout.animation;

import com.imperial.heap3d.snapshot.Node;

public class DeleteAnimation extends AnimationEvent{

	private float decScale;
	
	public DeleteAnimation(Node nodeToDelete) {
		this.shape = nodeToDelete.getGeometry();
		decScale = -nodeToDelete.getGeometry().getEntity().getScale()/ANIMATION_TIME;
	}

	@Override
	public void step(){
		float newScale = shape.getEntity().getScale()+decScale;
		shape.getEntity().setScale(newScale < 0 ? 0 : newScale);
		itteration++;
	}
	
}
