package com.perf;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Pyramid;
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
        Pyramid c1 = new Pyramid(0, 0, 110, 0, 0, 0, 0.1f, col1);
        Line l1 = new Line(0, -20, 110, 0, 0, 110, Colour.GREEN);
        Cube c2 = new Cube(0, 1, 110, 0, 0, 0, 1, Colour.AQUA);
        addShapeTo3DSpace(c1);
        addShapeTo3DSpace(c2);
        addShapeTo3DSpace(l1);
        /*
        text = getText3D();
        try {
        	text.print(10, 10, 0, 45, 45, 0, 1, Colour.GREEN, "ABCDEFGHIJKLMNOPQRSTUVWXYZ_$abcdefghijklmnopqrstuvwxyz");
        }catch (Exception e) {
			e.printStackTrace();
		}
		*/
    }

    @Override
    protected void inLoop() {
    }

    @Override
    protected void afterLoop() {

    }
}
