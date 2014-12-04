package com.imperial.heap3d.implementations.layout.animation;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.snapshot.Node;
import org.lwjgl.util.vector.Vector3f;

public class MoveAnimation extends AnimationEvent {

	private float incrementX, incrementY, incrementZ;
	private Vector3f finalPosition;
	private Shape shape;

	public MoveAnimation(Node startNode, Node endNode) {
		shape = endNode.getGeometry();
		Shape oldShape = startNode.getGeometry();
		float[] oldPos = oldShape.getPosition();
		float[] newPos = shape.getPosition();
		incrementX = (newPos[0] - oldPos[0]) / maxIterations;
		incrementY = (newPos[1] - oldPos[1]) / maxIterations;
		incrementZ = (newPos[2] - oldPos[2]) / maxIterations;
		finalPosition = new Vector3f(newPos[0], newPos[1], newPos[2]);
		shape.setPosition(oldPos[0], oldPos[1], oldPos[2]);
	}

	@Override
	protected void executeStep() {
		shape.getEntity().increasePosition(incrementX, incrementY, incrementZ);
	}

	@Override
	public void finish() {
		shape.setPosition(finalPosition.getX(), finalPosition.getY(), finalPosition.getZ());
	}
}