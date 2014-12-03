package com.imperial.heap3d.implementations.layout;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.heap3d.layout.Graph;
import com.imperial.heap3d.implementations.snapshot.Node;

public class HeapEdge {

    public HeapEdge()
    {

    }

    private Line line;

    public void connect(Node from, Node to, Colour color, RenderEngine world) {
        Cube fromCube = (Cube) from.getGeometry();
        Cube toCube = (Cube) to.getGeometry();
        line = new Line(fromCube, toCube, color);
        world.addShapeTo3DSpace(line);
    }

    public Line getLine()
    {
        return line;
    }
}
