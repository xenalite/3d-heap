package com.graphics;

import java.awt.color.CMMException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Camera;
import com.graphics.entities.Light;
import com.graphics.rendering.MasterRenderer;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Shape;

public abstract class RenderEngine {

	private MasterRenderer renderer;
	private Light sun = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
	private long lastFps;
	private int fpsInc, fps;
	private List<Shape> entities = new ArrayList<Shape>();
	private boolean breakFromLoop;
	
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
		
		while(!Display.isCloseRequested() && !breakFromLoop){
			
			camera.move();
			
			inLoop();
			
			for(Shape e : entities){
				renderer.processEntity(e.getEntity());
				for(Line c : ((Cube)e).getLinks().values())
					renderer.processEntity(c.getEntity());
			}
			
			renderer.render(sun, camera);
			DisplayManager.updateDisplay();
			updateFPS();
			//System.out.println(camera.);
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
	
	protected void addEntityTo3DSpace(Shape e){
		entities.add(e);
	}
	
	protected void removeEntityFrom3DSpace(Shape e){
		entities.remove(e);
	}
	
	protected void clearEntitiesFrom3DSpace(){
		entities.clear();
	}
	
	protected int getNumberOfEntities() {
		return entities.size();
	}
	
	private void cleanUp(){
		renderer.cleanUp();
		//EntityBuilder.getBuilder().destroyBuilder();
		DisplayManager.closeDisplay();
	}
	
	private void updateFPS() {
	    if (getTime() - lastFps > 1000) {
	    	fps = fpsInc;
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
		renderer.setBackR(r);
		renderer.setBackG(g);
		renderer.setBackB(b);
		renderer.setBackA(a);
	}
}
