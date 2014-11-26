package com.graphics.shapes;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Cube extends Shape {

	private static final float[] vertices = new float[]{
			// Back
			-0.5f, 0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,

			// Front
			-0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f,
			0.5f, -0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,

			// Right
			0.5f, 0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,

			// Left
			-0.5f, 0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f, -0.5f, 0.5f,
			-0.5f, 0.5f, 0.5f,

			// Top
			-0.5f, 0.5f, 0.5f,
			-0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, 0.5f,

			// Bottom
			-0.5f, -0.5f, 0.5f,
			-0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, 0.5f
	};

	private static final float[] normals = new float[]{
			0f, 0f, -1f,
			0f, 0f, -1f,
			0f, 0f, -1f,
			0f, 0f, -1f,

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
			0, 3, 1,
			3, 2, 1,
			// Front
			4, 5, 7,
			7, 5, 6,
			// Right
			8, 11, 9,
			11, 10, 9,
			// Left
			12, 13, 15,
			15, 13, 14,
			// Top
			17, 16, 19,
			19, 18, 17,
			// Bottom
			20, 21, 23,
			23, 21, 22

	};

	private final float[] colourValues = new float[vertices.length];

	private static Map<Colour, RawModel> usedModels = new HashMap<Colour, RawModel>();

	public Cube(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour) {

		super(colour, vertices, normals, indices);

		RawModel m = usedModels.get(colour);

		if (m != null) {
			entity = new Entity(m, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
			return;
		}

		for (int i = 0; i < colourValues.length; i += 3) {
			colourValues[i] = colour.getR();
			colourValues[i + 1] = colour.getG();
			colourValues[i + 2] = colour.getB();
		}

		RawModel model = Loader.getInstance().loadToVAO(vertices, colourValues, normals, indices);

		usedModels.put(colour, model);

		entity = new Entity(model, new Vector3f(x, y, z), rotX, rotY, rotZ, scale, GL11.GL_TRIANGLES);
	}
}
