package com.graphics;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Camera;
import com.graphics.entities.Light;
import com.graphics.rendering.MasterRenderer;
import com.graphics.shapes.Shape;

public abstract class RenderEngine implements Runnable{

	private MasterRenderer renderer;
	private Light sun = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
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
		
		renderer.setBackR(r);
		renderer.setBackG(g);
		renderer.setBackB(b);
		renderer.setBackA(a);
		
		Camera camera = new Camera();
		
		lastFps = getTime();
		
		beforeLoop();
		
		while(!Display.isCloseRequested() && !breakFromLoop){
			
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
		}
		
		afterLoop();
		cleanUp();
	}
	
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
}
