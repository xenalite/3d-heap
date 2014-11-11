package com.graphics;

import org.lwjgl.util.vector.Vector3f;
import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import com.graphics.models.TexturedModel;
import com.graphics.textures.ModelTexture;
import com.graphics.utils.OBJLoader;

public class EntityBuilder {

	private static EntityBuilder instance = new EntityBuilder();;
	
	//private Loader loader = new Loader();;
	private TexturedModel texturedModel;
	
	private float x, y, z = -50f;
	private float rx, ry, rz;
	private float scale = 1;
	
	private EntityBuilder(){}
	
	public static EntityBuilder getBuilder(){
		return instance;
	}
	/*
	public EntityBuilder withTexturedModel(String modelFilePath, String textureFilePath, int shineDamper, int reflectivity){
		RawModel model = OBJLoader.loadObjModel(modelFilePath, loader);
		
		
		
		texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(textureFilePath)));
		texturedModel.getTexture().setShineDamper(shineDamper);
		texturedModel.getTexture().setReflectivity(reflectivity);
		return this;
	}
	
	public EntityBuilder setPosition(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public EntityBuilder setRotation(float rx, float ry, float rz){
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		return this;
	}

	public EntityBuilder setScale(float scale) {
		this.scale = scale;
		return this;
	}

	public Entity build(){
		Entity entity = new Entity(texturedModel, new Vector3f(x, y, z), rx, ry, rz, scale);
		return entity;
	}

	public void destroyBuilder(){
		if(loader != null){
			loader.cleanUp();
			loader = null;
		}
	}
	
	public void resetValues(){
		x = y = z = rx = ry = rz = 0f;
	}
	*/
}
