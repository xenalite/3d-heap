package com.graphics.shapes;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;

public class Cube extends Shape{

	private static final float[] vertices = new float[]{			
			// Back
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		

			// Front
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,

			// Right
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,

			// Left
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,

			// Top
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,

			// Bottom
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
	};

	private static final float[] normals = new float[]{			
			0f, 0f,-1f,	
			0f, 0f,-1f,	
			0f, 0f,-1f,	
			0f, 0f,-1f,		

			0f, 0f, 1f,	
			0f, 0f, 1f,	
			0f, 0f, 1f,	
			0f, 0f, 1f,

			1f, 0f, 0f,	
			1f, 0f, 0f,	
			1f, 0f, 0f,	
			1f, 0f, 0f,	

			-1f, 0f, 0f,	
			-1f, 0f, 0f,
			-1f, 0f, 0f,
			-1f, 0f, 0f,

			0f, 1f, 0f,	
			0f, 1f, 0f,	
			0f, 1f, 0f,		
			0f, 1f, 0f,	

			0f, -1f, 0f,		
			0f, -1f, 0f,	
			0f, -1f, 0f,
			0f, -1f, 0f,
	};

	private static final int[] indices = new int[]{
			// Back
			0,3,1,	
			3,2,1,	
			// Front
			4,5,7,
			7,5,6,
			// Right
			8,11,9,
			11,10,9,
			// Left
			12,13,15,
			15,13,14,
			// Top
			17,16,19,
			19,18,17,
			// Bottom
			20,21,23,
			23,21,22

	};

	private final float[] colourValues = new float[vertices.length];
	
	private static Map<Colour, RawModel> usedModels = new HashMap<Colour, RawModel>(); 
	
	public Cube(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour){
		
		super(colour, vertices, normals, indices);
		
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
		
		RawModel model = Loader.getInstance().loadToVAO(vertices, colourValues, normals, indices);
		
		usedModels.put(colour, model);
		
		entity = new Entity(model, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
	}

	/*
	private Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {

	    //Create normal vector we are going to output.
	    Vector3f output = new Vector3f();

	    //Calculate vectors used for creating normal (these are the edges of the triangle).
	    Vector3f calU = new Vector3f(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
	    Vector3f calV = new Vector3f(p3.x-p1.x, p3.y-p1.y, p3.z-p1.z);

	    //The output vector is equal to the cross products of the two edges of the triangle
	    output.x = calU.y*calV.z - calU.z*calV.y;
	    output.y = calU.z*calV.x - calU.x*calV.z;
	    output.z = calU.x*calV.y - calU.y*calV.x;

	    output.normalise(output);
	    
	    //Return the resulting vector.
	    return output;//.normalise(output);
	}
	
	private float[] getNormals(){
		float[] normals = new float[indices.length];
		for(int i = 0; i < indices.length; i+=6){
			Vector3f p11 = new Vector3f(vertices[indices[i]], vertices[indices[i]], vertices[indices[i]]);
			Vector3f p12 = new Vector3f(vertices[indices[i+1]], vertices[indices[i+1]], vertices[indices[i+1]]);
			Vector3f p13 = new Vector3f(vertices[indices[i+2]], vertices[indices[i+2]], vertices[indices[i+2]]);
			
			Vector3f p21 = new Vector3f(vertices[indices[i+3]], vertices[indices[i+3]], vertices[indices[i+3]]);
			Vector3f p22 = new Vector3f(vertices[indices[i+4]], vertices[indices[i+4]], vertices[indices[i+4]]);
			Vector3f p23 = new Vector3f(vertices[indices[i+5]], vertices[indices[i+5]], vertices[indices[i+5]]);
			
			Vector3f v1 = getNormal(p11, p12, p13);
			Vector3f v2 = getNormal(p21, p22, p23);
			
			System.out.println(v1.x + ", "+v1.y+ ", "+v1.z);
			
			normals[i] =  v1.x;
			normals[i+1] = v1.y;
			normals[i+2] = v1.z;
			normals[i+3] = v2.x;
			normals[i+4] = v2.y;
			normals[i+5] = v2.z;
		}
		return normals;
	}
	*/
}
