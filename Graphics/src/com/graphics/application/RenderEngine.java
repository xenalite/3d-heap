package com.graphics.application;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import com.graphics.entities.Camera;
import com.graphics.entities.Entity;
import com.graphics.entities.Light;

public abstract class RenderEngine {

	private MasterRenderer renderer;
	private Light sun = new Light(new Vector3f(3000, 3000, 3000), new Vector3f(1, 1, 1));
	private long lastFps;
	private int fps;
	private List<Entity> entities = new ArrayList<Entity>();
	
	public RenderEngine(String title, int width, int height, boolean resizable){
		DisplayManager.createDisplay(title, width, height, resizable);
		renderer = new MasterRenderer();
	}
	
	protected void setMainLight(float x, float y, float z, float r, float g, float b){
		sun = new Light(new Vector3f(x, y, z), new Vector3f(r, g, b));
	}
	
	protected void start(){
		
		Camera camera = new Camera();
		
		lastFps = getTime();
		
		beforeLoop();
		
		while(!Display.isCloseRequested()){
			
			camera.move();
			
			inLoop();
			
			for(Entity e : entities){
				renderer.processEntity(e);
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
	
	protected void addEntityTo3DSpace(Entity e){
		entities.add(e);
	}
	
	protected int getNumberOfEntities() {
		return entities.size();
	}
	
	private void cleanUp(){
		renderer.cleanUp();
		EntityBuilder.getBuilder().destroyBuilder();
		DisplayManager.closeDisplay();
	}
	
	private void updateFPS() {
	    if (getTime() - lastFps > 1000) {
	        Display.setTitle("FPS: " + fps); 
	        fps = 0;
	        lastFps += 1000;
	    }
	    fps++;
	}
	
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	protected void setBackgroundColour(float r, float g, float b, float a) {
		renderer.setBackR(r);
		renderer.setBackG(g);
		renderer.setBackB(b);
		renderer.setBackA(a);
	}
}
