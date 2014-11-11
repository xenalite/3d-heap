package com.graphics.shapes;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;

public class Line extends Shape{

	public Line(Entity e1, Entity e2, Colour colour){
		
		float[] vertices = new float[]{			
				e1.getPosition().x, e1.getPosition().y, e1.getPosition().z,
				e2.getPosition().x, e2.getPosition().y, e2.getPosition().z
		};
		
	
		
		int[] indices = new int[]{0, 1};
		
		float[] colourValues = new float[vertices.length];
		
		for(int i = 0; i < colourValues.length; i+=3){
			colourValues[i] = colour.getR();
			colourValues[i+1] = colour.getG();
			colourValues[i+2] = colour.getB();
		}
		

		RawModel model = Loader.getInstance().loadToVAO(vertices, colourValues, indices);
		
		entity = new Entity(model, new Vector3f(0,0,0), 0, 0, 0, 1, GL11.GL_LINES);
	}
	
}
