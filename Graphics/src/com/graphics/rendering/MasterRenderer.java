package com.graphics.rendering;

import com.graphics.entities.Camera;
import com.graphics.entities.Entity;
import com.graphics.entities.Light;
import com.graphics.models.RawModel;
import com.graphics.shaders.StaticShader;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	private float backR, backG, backB, backA;
	private Map<RawModel, List<Entity>> entities = new HashMap<RawModel, List<Entity>>();
	
	public void render(Light sun, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColour(backR, backG, backB);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
		GL11.glClearColor(backR, backG, backB, backA);
	}

	public void processEntity(Entity entity){
		RawModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp(){
		shader.cleanUP();
	}
	
	public void setBackR(float backR) {
		this.backR = backR;
	}

	public void setBackG(float backG) {
		this.backG = backG;
	}

	public void setBackB(float backB) {
		this.backB = backB;
	}

	public void setBackA(float backA) {
		this.backA = backA;
	}

	public void setLightVars(float damper, float reflectivity) {
		renderer.setLightVars(damper, reflectivity);
	}
	
}
