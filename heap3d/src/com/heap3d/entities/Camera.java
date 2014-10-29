package com.heap3d.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch, yaw, roll;
	
	private static final float SPEED = 0.2f;
	
	public Camera() {
	}
	
	public void move(){
		
		float multi = 1;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			multi = 3;
		
		float total = multi * SPEED;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += total;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= total;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += total;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= total;

		
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
}
