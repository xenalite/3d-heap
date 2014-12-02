package com.graphics.shapes;

import com.graphics.entities.Entity;
import com.graphics.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Letter extends Shape {

	private final float[] colourValues = new float[vertices.length];

	public Letter(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour, RawModel model) {

		super(colour, null, null, null);

		for (int i = 0; i < colourValues.length; i += 3) {
			colourValues[i] = colour.getR();
			colourValues[i + 1] = colour.getG();
			colourValues[i + 2] = colour.getB();
		}

		entity = new Entity(model, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
	}
}
