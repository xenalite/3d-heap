package com.perf;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;

/**
 * Created by costica1234 on 26/11/14.
 */
public class SimpleTest extends RenderEngine {

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
        Colour col2 = new Colour(0, 1, 0);
        Colour col3 = new Colour(0, 0, 1);

        Cube c1 = new Cube(10, 20, -40, 10, 20, 30, 10, col1);
        Cube c2 = new Cube(50, 80, 0, 10, 20, 30, 10, col2);
        Cube c3 = new Cube(30, 50, -20, 50, 0, 80, 10, col3);

        addShapeTo3DSpace(c1);
        addShapeTo3DSpace(c2);
        addShapeTo3DSpace(c3);

        Line l = new Line(c1, c2, Colour.BLUE);

        addShapeTo3DSpace(l);
    }

    @Override
    protected void inLoop() {

    }

    @Override
    protected void afterLoop() {

    }
}
