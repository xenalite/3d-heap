package com.graphics.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.graphics.entities.Camera;
import com.graphics.entities.Entity;
import com.graphics.entities.Light;
import com.graphics.models.RawModel;
import com.graphics.models.TexturedModel;
import com.graphics.shaders.StaticShader;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<RawModel, List<Entity>> entities = new HashMap<RawModel, List<Entity>>();
	
	public void render(Light sun, Camera camera){
		renderer.prepare();
		shader.start();
		//shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	public void render(Camera camera){
		renderer.prepare();
		shader.start();
	//	shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
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
		renderer.setBackR(backR);
	}

	public void setBackG(float backG) {
		renderer.setBackG(backG);
	}

	public void setBackB(float backB) {
		renderer.setBackB(backB);
	}

	public void setBackA(float backA) {
		renderer.setBackA(backA);
	}
	
}
