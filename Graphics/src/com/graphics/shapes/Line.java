package com.graphics.shapes;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;

public class Line extends Shape{

	public Line(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, Colour colour){
		super(colour);
		setupLine(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), colour);
	}

	public Line(Cube cubeA, Cube cubeB, Colour colour){
		super(colour);

		Entity e1 = cubeA.getEntity();
		Entity e2 = cubeB.getEntity();
		setupLine(e1.getPosition(), e2.getPosition(), colour);
	}

	private void setupLine(Vector3f a, Vector3f b, Colour colour){

		float[] vertices = new float[]{			
				a.x, a.y, a.z,
				b.x, b.y, b.z
		};

		float[] normals = new float[]{			
				0f, 0f, 0f,	
				0f, 0f, 0f,
		};

		int[] indices = new int[]{0, 1};

		setShapeAttribs(vertices, normals, indices);

		float[] colourValues = new float[vertices.length];

		for(int i = 0; i < colourValues.length; i+=3){
			colourValues[i] = colour.getR();
			colourValues[i+1] = colour.getG();
			colourValues[i+2] = colour.getB();
		}

		RawModel model = Loader.getInstance().loadToVAO(vertices, colourValues, normals, indices);
		entity = new Entity(model, new Vector3f(0,0,0), 0, 0, 0, 1, GL11.GL_LINES);
	}

}
