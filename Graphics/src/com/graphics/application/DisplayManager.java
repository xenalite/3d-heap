package com.graphics.application;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int FPS_CAP = 120;
	
	/**
	 * Creates a window
	 * @param title
	 * @param width
	 * @param height
	 * @param resizable
	 */
	public static void createDisplay(String title, int width, int height, boolean resizable){
		
		ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
				.withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.create(new PixelFormat(), contextAtrributes);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		GL11.glViewport(0, 0, width, height);
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
	
}
