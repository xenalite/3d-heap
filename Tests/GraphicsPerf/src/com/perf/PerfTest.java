package com.perf;

import java.util.Random;
import com.graphics.application.EntityBuilder;
import com.graphics.application.RenderEngine;

public class PerfTest extends RenderEngine{

	private Random rand;
	EntityBuilder e;
	
	public static void main(String[] args){
		new PerfTest();
	}
	
	public PerfTest() {
		super("Test", 1280, 720, false);
		super.setBackgroundColour(1f, 0f, 0f, 1f);
		super.start();
	}
	
	@Override
	protected void beforeLoop() {
		rand = new Random();
		e = EntityBuilder.getBuilder().withTexturedModel("models/cube.obj", "textures/white.png", 10, 1);
	}

	@Override
	protected void inLoop() {
		
		float x = rand.nextFloat() * 100 -50;
		float y = rand.nextFloat() * 100 -50;
		float z = rand.nextFloat() * -300;
		addEntityTo3DSpace(e.setPosition(x, y, z).setRotation( rand.nextFloat()*180f,  rand.nextFloat()*180f, 0f).build());
	}

	@Override
	protected void afterLoop() {
		
	}
	
}
