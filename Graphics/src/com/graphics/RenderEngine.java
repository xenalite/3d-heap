package com.graphics;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.graphics.entities.Camera;
import com.graphics.entities.Light;
import com.graphics.raycasting.Ray;
import com.graphics.raycasting.RayCastUtil;
import com.graphics.rendering.MasterRenderer;
import com.graphics.rendering.Renderer;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Line;
import com.graphics.shapes.Shape;
import com.graphics.utils.Maths;

public abstract class RenderEngine implements Runnable{

	private MasterRenderer renderer;
	private Light sun = new Light(new Vector3f(40, 40, 40), new Vector3f(1, 1, 1));
	private long lastFps;
	private int fpsInc, fps;
	private List<Shape> shapes = new ArrayList<Shape>();
	private boolean breakFromLoop;
	private Screenshot screenshot = new Screenshot();
	
	private String title ="";
	private int width, height;
	private boolean resizable;
	private final Canvas canvas;
	private float r, g, b, a;
	private Camera camera;
	
	public RenderEngine(String title, int width, int height, boolean resizable){
		this.title = title;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		canvas = null;
	}
	
	public RenderEngine(final Canvas canvas){
		this.canvas = canvas;
	}
	
	protected void setMainLight(float x, float y, float z, float r, float g, float b, float damper, float refelectivity){
		sun = new Light(new Vector3f(x, y, z), new Vector3f(r, g, b));
		renderer.setLightVars(damper, refelectivity);
	}
	
	@Override
	public void run() {
		
		if(canvas == null)
			DisplayManager.createDisplay(title, width, height, resizable);
		else
			DisplayManager.createDisplay(canvas);
		
		renderer = new MasterRenderer();
		camera = new Camera();
		
		lastFps = getTime();
		
		beforeLoop();
		
		while(!Display.isCloseRequested() && !breakFromLoop){
			
			renderer.setBackR(r);
			renderer.setBackG(g);
			renderer.setBackB(b);
			renderer.setBackA(a);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_P))
				screenshot.capture();
			
			camera.move();
			
			inLoop();
			
			for(Shape e : shapes){
				renderer.processEntity(e.getEntity());
			}
			
			renderer.render(sun, camera);
			DisplayManager.updateDisplay();
			updateFPS();
			
			// ##################################
			// Ray Picking - Needs fixing
			if(!Mouse.isButtonDown(1))
				down = false;
			Ray r = new Ray();
			r.createRay(camera);
			if(Mouse.isButtonDown(1) && !down){
				down = true;
				
				
			//	Ray r = new Ray();
			//	r.createRay(camera);
				//---
		//		for(Shape e : shapes){

				System.out.println(r.origin + ", "+ r.direction);
					Vector3f vec = RayCastUtil.rayTest(r.origin, r.direction, shapes.get(0), true);
					System.out.println(vec);
					System.out.println("\n======================\n");
					
		//		}
				
			}
			// ##################################
			//System.out.println(r.direction);
		}
		
		afterLoop();
		cleanUp();
	}
	
	boolean down;
	protected abstract void beforeLoop();
	protected abstract void inLoop();
	protected abstract void afterLoop();
	
	protected int getFPS(){
		return fps;
	}
	
	protected void breakOutOfLoop(){
		breakFromLoop = true;
	}
	
	protected void addShapeTo3DSpace(Shape e){
		shapes.add(e);
	}
	
	protected void removeShapeFrom3DSpace(Shape e){
		shapes.remove(e);
	}
	
	protected void clearShapesFrom3DSpace(){
		shapes.clear();
	}
	
	protected int getNumberOfShapes() {
		return shapes.size();
	}
	
	private void cleanUp(){
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private void updateFPS() {
	    if (getTime() - lastFps > 1000) {
	    	fps = fpsInc;
	    	if(canvas != null)
	    		Display.setTitle("FPS: " + fps); 
	        fpsInc = 0;
	        lastFps += 1000;
	    }
	    fpsInc++;
	}
	
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	protected void setBackgroundColour(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	protected void captureScreen(String pathFromWorkspace, String filenameWithoutExtention){
		screenshot.capture(pathFromWorkspace, filenameWithoutExtention);
	}
	
	protected void setCameraPosition(float x, float y, float z){
		if(camera == null)
			return;
		camera.setPosition(new Vector3f(x, y, z));
	}
	
	protected void moveCameraPosition(float dx, float dy, float dz){
		if(camera == null)
			return;
		Vector3f oldPos = camera.getPosition();
		camera.setPosition(new Vector3f(oldPos.x+dx, oldPos.y+dy, oldPos.z+dz));
	}
}
