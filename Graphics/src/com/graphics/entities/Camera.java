package com.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private static final float MOVE_TIME = 100;
	
	private Vector3f position = new Vector3f(0, 0, 120);
	
	private boolean smoothMove;
	private float incX, incY, incZ, timeStep, incPitch, incYaw;
	private float pitch, yaw, roll;
	
	public Camera() {
	}
	
	public void move(){
		
		if(smoothMove){
			position.x += incX;
			position.y += incY;
			position.z += incZ;
			yaw -= incYaw;
			pitch -= incPitch;
			timeStep++;
			if(timeStep >= MOVE_TIME)
				smoothMove = false;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos){
		position = pos;
	}
	
	public void setPositionSmooth(Vector3f pos){
		timeStep = 0f;
		incX = (pos.x - position.x)/MOVE_TIME;
		incY = (pos.y - position.y)/MOVE_TIME;
		incZ = (pos.z - position.z)/MOVE_TIME;
		incPitch = pitch/MOVE_TIME;
		incYaw = yaw/MOVE_TIME;
		smoothMove = true;
	}
	
	public void moveBack(float movement){
		position.x += movement * (float)Math.sin(Math.toRadians(yaw+180));
		position.y += movement * (float)Math.tan(Math.toRadians(pitch+180));
		position.z += movement * (float)Math.cos(Math.toRadians(yaw));
		smoothMove = false;
	}
	
	public void moveForward(float movement){
    	position.x -= movement * (float)Math.sin(Math.toRadians(yaw+180));
    	position.y -= movement * (float)Math.tan(Math.toRadians(pitch+180));
	    position.z -= movement * (float)Math.cos(Math.toRadians(yaw));
	    smoothMove = false;
	}
	
	public void moveLeft(float movement){
    	position.x -= movement * (float)Math.sin(Math.toRadians(yaw+90));
    	position.z -= movement * (float)Math.cos(Math.toRadians(yaw-90));
    	smoothMove = false;
	}
	
	public void moveRight(float movement){
		position.x += movement * (float)Math.sin(Math.toRadians(yaw+90));
		position.z += movement * (float)Math.cos(Math.toRadians(yaw-90));
		smoothMove = false;
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

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
}
