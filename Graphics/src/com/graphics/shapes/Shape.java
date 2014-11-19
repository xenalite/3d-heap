package com.graphics.shapes;

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
}