package com.graphics.entities;

import org.lwjgl.util.vector.Vector3f;
import com.graphics.models.RawModel;

/**
 * Instance of a textured model
 * @author Stephen James
 *
 */
public class Entity {

	private RawModel model;
	private Vector3f position;
	private float rx, ry, rz, scale;
	private int drawMode;
	
	public Entity(RawModel model, Vector3f position, float rx, float ry,
			float rz, float scale, int drawMode) {
		this.model = model;
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.drawMode = drawMode;
	}
	
	public int getDrawMode(){
		return drawMode;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rx += dx;
		this.ry += dy;
		this.rz += dz;
	}
	
	public RawModel getModel() {
		return model;
	}

	public void setModel(RawModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
}
