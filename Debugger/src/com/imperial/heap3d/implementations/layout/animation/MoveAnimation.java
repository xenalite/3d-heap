package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.snapshot.Node;

public class MoveAnimation extends AnimationEvent{

	private float incX, incY, incZ;
	
	public MoveAnimation(Node startState, Node endState) {
		
		this.shape = endState.getGeometry();
		
		Shape oldShape = startState.getGeometry();
		float[] oldPos = oldShape.getPosition();
		float[] newPos = shape.getPosition();
		incX = (newPos[0]-oldPos[0])/ANIMATION_TIME;
		incY = (newPos[1]-oldPos[1])/ANIMATION_TIME;
		incZ = (newPos[2]-oldPos[2])/ANIMATION_TIME;
		shape.setPosition(oldPos[0], oldPos[1], oldPos[2]);
	}

	@Override
	public void step(){
		shape.getEntity().increasePosition(incX, incY, incZ);
		itteration++;
	}
	
}
