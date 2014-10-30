package com.graphics.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import com.graphics.entities.Camera;
import com.graphics.entities.Entity;
import com.graphics.entities.Light;
import com.graphics.models.RawModel;
import com.graphics.models.TexturedModel;
import com.graphics.textures.ModelTexture;
import com.graphics.utils.OBJLoader;

public class MainProgramLoop {

	private RawModel model;
	private MasterRenderer renderer;
	private TexturedModel texturedModel;
	
	private long lastFps;
	private int fps;
	
	public MainProgramLoop(){
		
		lastFps = getTime();
		
		DisplayManager.createDisplay();
		
		renderer = new MasterRenderer();
		Loader loader = new Loader();
		
		/*
		
		float[] vertices = {
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		};
		
		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0
		};
		
		*/
		
		model = OBJLoader.loadObjModel("models/cube.obj", loader);//loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white.png"));
		texturedModel = new TexturedModel(model, texture);
		
		loop();
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private void loop(){
		
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(50);
		texture.setReflectivity(1);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(3000, 3000, 3000), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		List<Entity> allCubes = new ArrayList<Entity>();
		Random rand = new Random();
		
		while(!Display.isCloseRequested()){
			entity.increaseRotation(0, 1, 0);
			camera.move();
			
			float x = rand.nextFloat() * 100 -50;
			float y = rand.nextFloat() * 100 -50;
			float z = rand.nextFloat() * -300;
			allCubes.add(new Entity(texturedModel, new Vector3f(x, y, z),  rand.nextFloat()*180f,  rand.nextFloat()*180f, 0f, 1));
			
			System.out.println("# Cubes: "+allCubes.size());
			
			for(Entity e : allCubes){
				renderer.processEntity(e);
			}
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
			updateFPS();
		}
	}
	
	private void updateFPS() {
	    if (getTime() - lastFps > 1000) {
	        Display.setTitle("FPS: " + fps); 
	        fps = 0;
	        lastFps += 1000;
	    }
	    fps++;
	}
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
