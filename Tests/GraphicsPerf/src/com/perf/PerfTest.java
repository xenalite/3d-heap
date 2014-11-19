package com.perf;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;

public class PerfTest extends RenderEngine{

	private Random rand;
	private Colour c;
	private Cube centre;
	private StringBuilder cubesStringBuilder;
	private static final int MAX_CUBES = 15000;
	
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
		cubesStringBuilder = new StringBuilder();
		System.out.println("\n### Starting cubes perf test ###\n");
	}

	private boolean finishedCubeTest, finishedLineTest;
	
	@Override
	protected void inLoop() {
	
		float x = rand.nextFloat() * 100 -50;
		float y = rand.nextFloat() * 100 -50;
		float z = rand.nextFloat() * -300;

		if(!finishedCubeTest){
			
			cubeTest(x, y, z);
			
			if(getNumberOfShapes() >= MAX_CUBES){
				finishedCubeTest = true;
				clearShapesFrom3DSpace();
				writeResults("cubes");
				cubesStringBuilder = new StringBuilder();
				System.out.println("\n### Starting lines perf test ###\n");
				centre = new Cube(0, 0, -150, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c);
				addShapeTo3DSpace(centre);
			}
			
		}else if(!finishedLineTest){

			lineTest(x, y, z);
			
			if(getNumberOfShapes() >= MAX_CUBES){
				finishedLineTest = true;
				writeResults("lines");
			}
			
		}else{
			breakOutOfLoop();
		}
		
	}

	@Override
	protected void afterLoop() {

	}
	
	private void writeResults(String filename){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filename+".data", "UTF-8");
			writer.print(cubesStringBuilder.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();
		}
	}
	
	private void cubeTest(float x, float y, float z){
		
		addShapeTo3DSpace(new Cube(x, y, z, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c));
		
		String output = getNumberOfShapes() + " " + getFPS() + "\n";
		
		if(getNumberOfShapes() % 50 == 0)
			System.out.print(output);
		
		cubesStringBuilder.append(output);
	}
	
	private void lineTest(float x, float y, float z){
		
		Cube cube = new Cube(x, y, z, rand.nextFloat()*180f, rand.nextFloat()*180f, 0f, 1f, c);
		addShapeTo3DSpace(cube);
		
		Line line = new Line(centre, cube, Colour.GREEN);
		addShapeTo3DSpace(line);
		
		String output = getNumberOfShapes() + " " + getFPS() + "\n";
		
		if(getNumberOfShapes() % 100 == 1)
			System.out.print(output);
		
		cubesStringBuilder.append(output);
	}
	
}
