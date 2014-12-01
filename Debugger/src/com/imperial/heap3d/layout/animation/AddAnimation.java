package com.imperial.heap3d.layout.animation;

import com.imperial.heap3d.snapshot.Node;

public class AddAnimation extends AnimationEvent{

	private float incScale;
	
	public AddAnimation(Node newNode) {
		
		this.shape = newNode.getGeometry();
		incScale = newNode.getGeometry().getEntity().getScale()/ANIMATION_TIME;
		shape.getEntity().setScale(0);
	}

	@Override
	public void step(){
		shape.getEntity().setScale(shape.getEntity().getScale()+incScale);
		itteration++;
	}
	
}
