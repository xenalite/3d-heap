package com.graphics.textures;

/**
 * Represent a texture we can use to texture our models
 * @author Stephen James
 */
public class ModelTexture {

	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public ModelTexture(int textureID){
		this.textureID = textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectiveity) {
		this.reflectivity = reflectiveity;
	}
	
}
