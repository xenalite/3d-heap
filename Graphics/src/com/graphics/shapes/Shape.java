package com.graphics.shapes;

import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;

public abstract class Shape {

	protected Entity entity;
	protected Colour colour;
	
	public Shape(Colour colour){
		this.colour = colour;
	}

	public Entity getEntity(){
		return entity;
	}

	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}
	
	public void setPosition(float x, float y, float z){
		entity.setPosition(new Vector3f(x, y, z));
	}
	
	public float[] getPosition(){
		Vector3f vPos = entity.getPosition();
		return new float[]{vPos.x, vPos.y, vPos.z};
	}
	
	public void setRotation(float rx, float ry, float rz){
		entity.setRx(rx);
		entity.setRy(ry);
		entity.setRz(rz);
	}
	
	public float[] getRotation(){
		return new float[]{entity.getRx(), entity.getRy(), entity.getRz()};
	}
}