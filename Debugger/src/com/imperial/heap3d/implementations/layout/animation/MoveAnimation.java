package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.shapes.Shape;
import org.lwjgl.util.vector.Vector3f;

public class MoveAnimation extends AnimationEvent {

	private float incrementX, incrementY, incrementZ;
	private Vector3f finalPosition;
	private Shape shape;

	public MoveAnimation(Shape startShape, Shape endShape) {
		shape = endShape;
		float[] oldPos = startShape.getPosition();
		float[] newPos = shape.getPosition();
		incrementX = (newPos[0] - oldPos[0]) / maxIterations;
		incrementY = (newPos[1] - oldPos[1]) / maxIterations;
		incrementZ = (newPos[2] - oldPos[2]) / maxIterations;
		finalPosition = new Vector3f(newPos[0], newPos[1], newPos[2]);
		shape.setPosition(oldPos[0], oldPos[1], oldPos[2]);
	}

	@Override
	public void step() {
		shape.getEntity().increasePosition(incrementX, incrementY, incrementZ);
		++iteration;
	}

	@Override
	public void finish() {
		shape.setPosition(finalPosition.getX(), finalPosition.getY(), finalPosition.getZ());
	}
}