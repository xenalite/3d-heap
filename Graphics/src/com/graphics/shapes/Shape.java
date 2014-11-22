package com.graphics.shapes;

import org.lwjgl.util.vector.Vector3f;

import com.graphics.entities.Entity;

public abstract class Shape {

	protected Entity entity;
	protected Colour colour;
	protected float[] vertices;
	protected float[] normals;
	protected int[] indices;
	
	public Shape(Colour colour){
		this.colour = colour;
	}
	
	public Shape(Colour colour, float[] vertices, float[] normals, int[] indices){
		this.colour = colour;
		this.vertices = vertices;
		this.normals = normals;
		this.indices = indices;
	}

	public void setShapeAttribs(float[] vertices, float[] normals, int[] indices){
		this.vertices = vertices;
		this.normals = normals;
		this.indices = indices;
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

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}
}