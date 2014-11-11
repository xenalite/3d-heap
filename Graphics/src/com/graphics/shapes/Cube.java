package com.graphics.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;

public class Cube extends Shape{
	
	private final float[] vertices = new float[]{			
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
	
	private final int[] indices = new int[]{
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
	
	private final float[] colourValues = new float[vertices.length];
	
	private static Map<Colour, RawModel> usedModels = new HashMap<Colour, RawModel>(); 
	
	private Map<Cube, Line> links = new HashMap<Cube, Line>();
	
	public Cube(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour){
		
		RawModel m = usedModels.get(colour);
		
		if(m!=null){
			entity = new Entity(m, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
			return;
		}
		
		for(int i = 0; i < colourValues.length; i+=3){
			colourValues[i] = colour.getR();
			colourValues[i+1] = colour.getG();
			colourValues[i+2] = colour.getB();
		}
		
		RawModel model = Loader.getInstance().loadToVAO(vertices, colourValues, indices);
		
		usedModels.put(colour, model);
		
		entity = new Entity(model, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
	}
	
	public void addConnection(Cube cubeToLink){
		Line l = new Line(getEntity(), cubeToLink.getEntity(), Colour.GREEN);
		links.put(cubeToLink, l);
	}
	
	//public boolean removeConnection(Cube cubeToRemove){
	//	return links.remove(cubeToRemove);
	//}
	
	public Map<Cube, Line> getLinks(){
		return links;
	}
}
