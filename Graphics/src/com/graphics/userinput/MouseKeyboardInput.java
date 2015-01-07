package com.graphics.userinput;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.graphics.entities.Camera;

public class MouseKeyboardInput extends Input{

	private static final float SPEED = 0.2f;
	private static final float SENSITIVITY = 0.05f;
	private static int MOUSE_LEFT = 0;
	
	private boolean started, stopped, paused, resumed, stepedOver, stepedInto, stepedOut;
	
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
	
	@Override
	public boolean hasStarted() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_1);
		boolean pressed = started && !down;
		started = down;
		return pressed;
	}

	@Override
	public boolean hasStopped() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_2);
		boolean pressed = stopped && !down;
		stopped = down;
		return pressed;
	}

	@Override
	public boolean hasPaused() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_3);
		boolean pressed = paused && !down;
		paused = down;
		return pressed;
	}

	@Override
	public boolean hasResumed() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_4);
		boolean pressed = resumed && !down;
		resumed = down;
		return pressed;
	}

	@Override
	public boolean hasStepedOver() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_5);
		boolean pressed = stepedOver && !down;
		stepedOver = down;
		return pressed;
	}

	@Override
	public boolean hasStepedInto() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_6);
		boolean pressed = stepedInto && !down;
		stepedInto = down;
		return pressed;
	}

	@Override
	public boolean hasStepedOut() {
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_7);
		boolean pressed = stepedOut && !down;
		stepedOut = down;
		return pressed;
	}

	
}
