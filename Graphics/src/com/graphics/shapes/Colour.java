package com.graphics.shapes;

/**
 * Some predefines colours to assign to shapes
 * @author Stephen James
 */
public class Colour {

	private final float r, g, b;
	
	public static final Colour RED = new Colour(1, 0, 0);
	public static final Colour GREEN = new Colour(0, 1, 0);
	public static final Colour BLUE = new Colour(0, 0, 1);
	public static final Colour YELLOW = new Colour(1, 1, 0);
	public static final Colour AQUA = new Colour(0, 1, 1);
	public static final Colour ORANGE = new Colour(1, 0.5f, 0);
	public static final Colour WHITE = new Colour(1f, 1f, 1f);
	
	public Colour(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	@Override
	public boolean equals(Object obj) {
		Colour c = (Colour)obj
;		return c.getR() == r && c.getG() == g && c.getB() == b;
	}
}
