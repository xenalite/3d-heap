package com.perf;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;

public class PerfTest extends RenderEngine{

	private Random rand;
	private Colour c;
	private StringBuilder stringBuilder;
	private static final int MAX_CUBES = 1000;
	
	public static void main(String[] args){
		new PerfTest();
	}
	
	public PerfTest() {
		super("Perf Test", 1280, 720, false);
		super.setBackgroundColour(0f, 0f, 0f, 1f);
		super.start();
	}
	
	@Override
	protected void beforeLoop() {
		rand = new Random();
		c = new Colour(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		stringBuilder = new StringBuilder();
	}

	
	@Override
	protected void inLoop() {
		
		int entities = getNumberOfEntities();
		
		if(entities > MAX_CUBES)
			breakOutOfLoop();
		
		float x = rand.nextFloat() * 100 -50;
		float y = rand.nextFloat() * 100 -50;
		float z = rand.nextFloat() * -300;
		
		addEntityTo3DSpace(new Cube(x, y, z, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c));
		
		String output = entities + " " + getFPS() + "\n";
		
		if(entities % 50 == 0)
			System.out.print(output);
		
		stringBuilder.append(output);
	}

	@Override
	protected void afterLoop() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("data.data", "UTF-8");
			writer.print(stringBuilder.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();
		}
	}
	
}
