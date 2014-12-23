package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.ColorConverter;

/**
 * Created by oskar on 06/12/14.
 */
public class GeometryFactory {

    private final static float STACK_NODE_SCALE = 5f;
    private final static float HEAP_NODE_SCALE = 0.5f;

    public static Shape createCubeForStackNode() {
        return new Cube(0, 0, 0, 0, 0, 0, STACK_NODE_SCALE, ColorConverter.randomColour());
    }

    public static Shape createCubeForObjectNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.ORANGE);
    }

    public static Shape createCubeForStringNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.RED);
    }

    public static Shape createCubeForArrayNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.YELLOW);
    }

    public static Shape createCubeForArrayElemNode() {
        return new Cube(0, 0, 0, 0, 0, 0, HEAP_NODE_SCALE, Colour.RED);
    }
}
