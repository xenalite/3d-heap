package com.perf;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.Display;

import com.graphics.EntityBuilder;
import com.graphics.RenderEngine;
import com.graphics.entities.Entity;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;

public class PerfTest extends RenderEngine{

	private Random rand;
	EntityBuilder e;
	Cube lastEn;
	public static void main(String[] args){
		new PerfTest();
	}
	
	public PerfTest() {
		super("Test", 1280, 720, false);
		super.setBackgroundColour(0f, 0f, 0f, 1f);
		super.start();
	}
	
	@Override
	protected void beforeLoop() {
		rand = new Random();
		c = new Colour(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());;
//		e = EntityBuilder.getBuilder().withTexturedModel("models/cube.obj", "textures/white.png", 10, 1);
		lastEn = new Cube(0, 0, -50, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c);
		addEntityTo3DSpace(lastEn);
	}

	int count = 0, count2 = 0;;
	Colour c;
	@Override
	protected void inLoop() {
		
		if(count > 200)
			super.breakOutOfLoop();
		
		float x = rand.nextFloat() * 100 -50;
		float y = rand.nextFloat() * 100 -50;
		float z = rand.nextFloat() * -300;
		
		Cube en = new Cube(x, y, z, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c);
		
		
		addEntityTo3DSpace(en);
		
		count++;
		System.out.println(super.getFPS());
	}

	@Override
	protected void afterLoop() {
		
	}
	
}
