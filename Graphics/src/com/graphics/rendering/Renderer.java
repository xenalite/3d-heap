package com.graphics.rendering;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import com.graphics.models.TexturedModel;
import com.graphics.shaders.StaticShader;
import com.graphics.textures.ModelTexture;
import com.graphics.utils.Maths;

public class Renderer {

	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 300f;//was 1000
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	public Renderer(StaticShader shader){
		
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<RawModel, List<Entity>> entities){
		
		for(RawModel model : entities.keySet()){
			
	//		prepareTextureModel(model);
			prepareModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch){
				prepareInstance(entity);
				GL11.glDrawElements(entity.getDrawMode(), model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindRawModel();
		}
	}
	
	/*
	private void prepareTextureModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		GL20.glEnableVertexAttribArray(0);	// points
		GL20.glEnableVertexAttribArray(1);	// texture coords
		GL20.glEnableVertexAttribArray(2);	// normals

		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
		
	}
	*/
	
	private void prepareModel(RawModel rawModel){
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);	// points
		GL20.glEnableVertexAttribArray(1);	// colours
		GL20.glEnableVertexAttribArray(2);	// normals
		shader.loadShineVariables(10, 1);
		//ModelTexture texture = model.getTexture();
		//shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		//GL13.glActiveTexture(GL13.GL_TEXTURE0);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
		
	}
	
	private void unbindRawModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void createProjectionMatrix(){
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);;
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
