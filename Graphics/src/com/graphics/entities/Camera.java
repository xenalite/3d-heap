package com.graphics.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 120);
	private float incX, incY, incZ, timeStep;
	private float pitch, yaw, roll;
	
	private static final float SPEED = 0.2f;
	private static float MOUSE_SENSITIVITY = 0.05f;
	private static final float MOVE_TIME = 100;
	private static int MOUSE_LEFT = 0;
	
	private boolean smoothMove;
	
	public Camera() {
	}
	
	public void move(){
		
		if(smoothMove){
			position.x += incX;
			position.y += incY;
			position.z += incZ;
			timeStep++;
			if(timeStep >= MOVE_TIME)
				smoothMove = false;
		}
		
		if(!Mouse.isGrabbed()){
			if(Mouse.isInsideWindow() && Mouse.isButtonDown(MOUSE_LEFT))
				Mouse.setGrabbed(true);
			else
				return;
		}
		
		float multi = 1;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			multi = 6;
		
		float total = multi * SPEED;
		
		int dx = Mouse.getDX();
        int dy = -Mouse.getDY();
        
        yaw += (dx * MOUSE_SENSITIVITY);
        pitch += (dy * MOUSE_SENSITIVITY);

        
        // Backwards
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
        	position.x += total * (float)Math.sin(Math.toRadians(yaw+180));
        	position.y += total * (float)Math.tan(Math.toRadians(pitch+180));
			position.z += total * (float)Math.cos(Math.toRadians(yaw));
			smoothMove = false;
        }
        
        // Forwards
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
        	position.x -= total * (float)Math.sin(Math.toRadians(yaw+180));
        	position.y -= total * (float)Math.tan(Math.toRadians(pitch+180));
		    position.z -= total * (float)Math.cos(Math.toRadians(yaw));
		    smoothMove = false;
        }
        
        // Right
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
        	position.x += total * (float)Math.sin(Math.toRadians(yaw+90));
			position.z += total * (float)Math.cos(Math.toRadians(yaw-90));
			smoothMove = false;
        }

        // Left
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
        	position.x -= total * (float)Math.sin(Math.toRadians(yaw+90));
        	position.z -= total * (float)Math.cos(Math.toRadians(yaw-90));
        	smoothMove = false;
        }
        
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Mouse.setGrabbed(false);
		
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos){
		position = pos;
	}
	
	public void setPositionSmooth(Vector3f pos){
		incX = (pos.x - position.x)/MOVE_TIME;
		incY = (pos.y - position.y)/MOVE_TIME;
		incZ = (pos.z - position.z)/MOVE_TIME;
		smoothMove = true;
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
