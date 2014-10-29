package com.heap3d.models;

import com.heap3d.textures.ModelTexture;

/**
 * Contains both the model and the texture, so that a texture can be associated
 * with a raw model.
 * 
 * @author Stephen James
 * 
 */
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
}
