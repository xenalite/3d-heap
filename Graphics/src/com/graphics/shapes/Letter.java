package com.graphics.shapes;

import com.graphics.entities.Entity;
import com.graphics.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Letter extends Shape {

	public Letter(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour, RawModel model) {

		super(colour, null, null, null);
		entity = new Entity(model, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
	}
}
