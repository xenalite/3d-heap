package com.graphics.userinput;

import java.io.IOException;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import com.graphics.entities.Camera;
import com.graphics.utils.ParseKinectData;
import com.graphics.utils.SocketHandler;

public class KinectInput extends Input{

	protected static final float SPEED = 0.2f;
	protected static final float SENSITIVITY = 0.2f;
	
	private SocketHandler movementHandler, speechHandler;
	private Thread movementThread, speechThread;
	private float lastX, lastY;
	private boolean firstReading = true;
	
	private float speedInc = 0;
	
	private boolean started, stopped, paused, resumed, stepedOver, stepedInto, stepedOut, exit, hasReset;
	
	public KinectInput() {
		try {
			movementHandler = new SocketHandler(4343);
			speechHandler = new SocketHandler(4344);
		} catch (IOException e) {
			System.err.println("Unable to enable kinect support");
			e.printStackTrace();
		}
		
		movementThread = new Thread(movementHandler);
		speechThread = new Thread(speechHandler);
		
		movementThread.start();
		speechThread.start();
	}
	
	@Override
	public void move(Camera camera) {
		
		String data = movementHandler.getMessage();
		String speechData = speechHandler.getMessage();
		speechHandler.resetMessage();
		
		if(data != null && !data.isEmpty()){

			Map<String, String> dic = ParseKinectData.parse(data);

			method1(camera, dic);
		}
		
		if(speechData != null && !speechData.isEmpty()){
		
			Map<String, String> dic = ParseKinectData.parse(speechData);
			String value = dic.get("speech");
			System.out.println("Said: " + dic.get("speech"));
			
			switch(value){
			case "CENTER":
				hasReset = true;
				camera.setPositionSmooth(new Vector3f(0, 0, 120));
				break;
			case "SPEED UP":
				speedInc+=0.1f;
				break;
			case "SLOW DOWN":
				if(speedInc-0.1f != -SPEED)
					speedInc-=0.1f;
				break;
			case "EXIT":
				exit = true;
				break;
			case "START":
				started = true;
				break;
			case "STOP":
				stopped = true;
				break;
			case "PAUSE":
				paused = true;
				break;
			case "RESUME":
				resumed = true;
				break;
			case "STEP OVER":
				stepedOver = true;
				break;
			case "STEP INTO":
				stepedInto = true;
				break;
			case "STEP OUT":
				stepedOut = true;
				break;
			}
			
		}
	}
	
	private float getValue(String value){
		float res = -1;
		try{
			res = Float.parseFloat(value);
		}catch(NumberFormatException e){}
		return res;
	}
	
	public boolean isEnabled(){
		return movementHandler.isConnected();
	}
	
	@Override
	public void cleanup(){
		movementHandler.stop();
		speechHandler.stop();
	}
	
	private void method1(Camera camera, Map<String, String> dic){
		
		if(!Boolean.parseBoolean(dic.get("engaged"))){
			firstReading = true;
			return;
		}

		float reach = getValue(dic.get("reach"));
		float finalSpeed = SPEED * reach + speedInc;
		
		if(reach > 0.5)
			camera.moveForward(finalSpeed);
		else if(reach < 0.05)
			camera.moveBack(SPEED);
		
		
		float x = getValue(dic.get("x")) * Display.getWidth();
		float y = getValue(dic.get("y")) * Display.getHeight();
		float dx = x - lastX;
		float dy = y - lastY;
		
		if(firstReading){
			dx = dy = 0;
			firstReading = false;
		}
		
		camera.setYaw(camera.getYaw() + dx * SENSITIVITY);
		camera.setPitch(camera.getPitch() + dy * SENSITIVITY);
		lastX = x;
		lastY = y;
	}

	@Override
	public boolean hasStarted() {
		boolean res = started;
		started = false;
		return res;
	}

	@Override
	public boolean hasStopped() {
		boolean res = stopped;
		stopped = false;
		return res;
	}

	@Override
	public boolean hasPaused() {
		boolean res = paused;
		paused = false;
		return res;
	}

	@Override
	public boolean hasResumed() {
		boolean res = resumed;
		resumed = false;
		return res;
	}

	@Override
	public boolean hasStepedOver() {
		boolean res = stepedOver;
		stepedOver = false;
		return res;
	}

	@Override
	public boolean hasStepedInto() {
		boolean res = stepedInto;
		stepedInto = false;
		return res;
	}

	@Override
	public boolean hasStepedOut() {
		boolean res = stepedOut;
		stepedOut = false;
		return res;
	}
	
	@Override
	public boolean hasExited() {
		boolean res = exit;
		exit = false;
		return res;
	}
	
	@Override
	public boolean hasReset() {
		boolean res = hasReset;
		hasReset = false;
		return res;
	}
}
