package com.graphics.movement;

import java.io.IOException;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import com.graphics.RenderEngine;
import com.graphics.entities.Camera;
import com.graphics.utils.ParseKinectData;
import com.graphics.utils.SocketHandler;

public class KinectInput extends Input{

	protected static final float SPEED = 0.2f;
	protected static final float SENSITIVITY = 5f;
	
	private SocketHandler movementHandler, speechHandler;
	private Thread movementThread, speechThread;
	private float lastLHx, lastLHy, lastRHx, lastRHy;
	private boolean firstReading = true;
	
	private float speedInc = 0;
	
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
				RenderEngine.breakFromLoop = true;
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
		
		float finalSpeed = SPEED + speedInc;
		
		switch(dic.get("lh_state")){
		case "Open":
		//	System.out.println("Open");
			camera.moveForward(finalSpeed);
			break;
		case "Closed":
		//	System.out.println("Closed");
			camera.moveBack(finalSpeed);
			break;
		case "Lasso":
		//	System.out.println("Lasso");
			break;
		case "Unknown":
		//	System.out.println("Unknown");
			break;
		default:
		//	System.out.println("Not Tracked");
			break;
		}
		
		switch(dic.get("rh_state")){
		case "Open":
		//	System.out.println("Open");
			break;
		case "Closed":
		//	System.out.println("Closed");
			break;
		case "Lasso":
			float x = getValue(dic.get("rh_x"));
			float y = getValue(dic.get("rh_y"));
			float dx = x - lastRHx;
			float dy = y - lastRHy;
			if(firstReading){
				dx = dy = 0;
				firstReading = false;
			}
			camera.setYaw(camera.getYaw() + dx * SENSITIVITY);
	        camera.setPitch(camera.getPitch() + dy * SENSITIVITY);
	        lastRHx = x;
	        lastRHy = y;
			break;
		case "Unknown":
		//	System.out.println("Unknown");
			break;
		default:
		//	System.out.println("Not Tracked");
			break;
		}
	}
}
