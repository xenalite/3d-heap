package com.graphics.movement;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.graphics.entities.Camera;

public class MouseKeyboardInput extends Input{

	private static final float SPEED = 0.2f;
	private static final float SENSITIVITY = 0.05f;
	private static int MOUSE_LEFT = 0;
	
	@Override
	public void move(Camera camera) {
		
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
        
        camera.setYaw(camera.getYaw() + dx * SENSITIVITY);
        camera.setPitch(camera.getPitch() + dy * SENSITIVITY);

        
        // Backwards
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        	camera.moveBack(total);
        
        
        // Forwards
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        	camera.moveForward(total);
        
        // Right
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        	camera.moveRight(total);

        // Left
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        	camera.moveLeft(total);
        
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Mouse.setGrabbed(false);
		
	}

	
}
