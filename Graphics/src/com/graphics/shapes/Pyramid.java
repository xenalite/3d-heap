package com.graphics.shapes;

import com.graphics.entities.Entity;
import com.graphics.models.Loader;
import com.graphics.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Pyramid extends Shape {

	private static final float[] vertices = new float[]{

		-0.5f, -0.5f, -0.5f,
		0.5f, -0.5f, -0.5f,
		0.5f, -0.5f, 0.5f,
		-0.5f, -0.5f, 0.5f,
		0, 1f, 0

	};

	private static final float[] normals = new float[]{
		-1f, -1f, -1f,
		1f, -1f, -1f,
		1f, -1f, 1f,
		-1f, -1f, 1f,
		0f, 1f, 0f
	};

	private static final int[] indices = new int[]{
		1, 4, 2,
		2, 4, 3,
		3, 4, 0,
		0, 4, 1,
		2, 3, 0,
		1, 2, 0

	};

	private final float[] colourValues = new float[vertices.length];

	private static Map<Colour, RawModel> usedModels = new HashMap<Colour, RawModel>();

	public Pyramid(float x, float y, float z, float rotX, float rotY, float rotZ, float scale, Colour colour) {

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
