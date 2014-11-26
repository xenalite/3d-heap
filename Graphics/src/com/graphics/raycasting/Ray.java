package com.graphics.raycasting;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.graphics.entities.Camera;
import com.graphics.rendering.Renderer;
import com.graphics.utils.Maths;

public class Ray {

	public Vector3f direction;
	public Vector3f origin;
	
	//This is correct... I think...
	public void createRay(Camera camera){
		float x = (2.0f * Mouse.getX()) / Display.getWidth() - 1.0f;
		float y = 1.0f - (2.0f * Mouse.getY()) / Display.getHeight();
		float z = 1.0f;
		
		origin = camera.getPosition();
		
		
		Vector3f ray_nds = new Vector3f(x, y, z);
		Vector4f ray_clip = new Vector4f(ray_nds.x, ray_nds.y, -1.0f, 1.0f);
		Matrix4f proj = Renderer.projectionMatrix;
		Vector4f ray_eye = new Vector4f();
		Matrix4f proj_invert = new Matrix4f();
		ray_eye = Matrix4f.transform(Matrix4f.invert(proj, proj_invert), ray_clip, ray_eye);
		ray_eye = new Vector4f(ray_eye.x, ray_eye.y, -1.0f, 0.0f);
		
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		Vector4f ray_wor_tmp = new Vector4f();
		ray_wor_tmp = Matrix4f.transform((Matrix4f) viewMatrix.invert(), ray_eye, ray_wor_tmp);
		direction = new Vector3f(ray_wor_tmp.x, ray_wor_tmp.y, ray_wor_tmp.z);
		direction = direction.normalise(direction);
	}
	
}
