package com.perf;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.text.Text3D;

/**
 * Created by costica1234 on 26/11/14.
 */
public class SimpleTest extends RenderEngine {

	Text3D text;
	
    public static void main(String[] args) {
        Thread t = new Thread(new SimpleTest(), "LwjglThread");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SimpleTest() {
        super("Simple Test", 1280, 720, false);
        super.setBackgroundColour(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    protected void beforeLoop() {
        Colour col1 = new Colour(1, 0, 0);
setRayPickDebugLines(true);
        Cube c1 = new Cube(0, 0, 110, 0, 0, 0, 1, col1);
        Cube c2 = new Cube(0, 0, 90, 0, 0, 0, 1.5f, Colour.AQUA);
        addShapeTo3DSpace(c1);
        addShapeTo3DSpace(c2);
        text = getText3D();
        try {
			text.print(10, 10, 0, 0, 0, 0, 1, Colour.GREEN, "$");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void inLoop() {

    }

    @Override
    protected void afterLoop() {

    }
}
