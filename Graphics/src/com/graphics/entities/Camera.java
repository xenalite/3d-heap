package com.graphics.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 120);
	private float pitch, yaw, roll;
	
	private static final float SPEED = 0.2f;
	private static float MOUSE_SENSITIVITY = 0.05f;
	
	private static int MOUSE_LEFT = 0;
	
	public Camera() {
	}
	
	public void move(){
		
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
        }
        
        // Forwards
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
        	position.x -= total * (float)Math.sin(Math.toRadians(yaw+180));
        	position.y -= total * (float)Math.tan(Math.toRadians(pitch+180));
		    position.z -= total * (float)Math.cos(Math.toRadians(yaw));
        }
        
        // Right
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
        	position.x += total * (float)Math.sin(Math.toRadians(yaw+90));
			position.z += total * (float)Math.cos(Math.toRadians(yaw-90));
        }

        // Left
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
        	position.x -= total * (float)Math.sin(Math.toRadians(yaw+90));
        	position.z -= total * (float)Math.cos(Math.toRadians(yaw-90));
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
