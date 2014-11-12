package com.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;
import com.graphics.entities.Camera;
import com.graphics.entities.Light;
import com.graphics.utils.Maths;

/**
 * Represents an instance of a shader program
 * @author Stephen James
 */
public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "shaders/colourVertexShader.txt";
	private static final String FRAGMENT_FILE = "shaders/colourFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	//private int location_lightPosition;
	//private int location_lightColour;
	//private int location_shineDamper;
	//private int location_reflectivity;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		//location_lightPosition = super.getUniformLocation("lightPosition");
		//location_lightColour = super.getUniformLocation("lightColour");
		//location_shineDamper = super.getUniformLocation("shineDamper");
		//location_reflectivity = super.getUniformLocation("reflectivity");
	}
	
	/*
	public void loadShineVariables(float damper, float refelectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refelectivity);
	}
	*/
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/*
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	*/
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
