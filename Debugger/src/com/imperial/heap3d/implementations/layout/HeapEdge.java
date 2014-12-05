package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.imperial.heap3d.implementations.snapshot.Node;

public class HeapEdge {

    private Line line;

    public void connect(Node from, Node to, Colour color, IRenderEngine renderEngine) {
        Cube fromCube = (Cube) from.getGeometry();
        Cube toCube = (Cube) to.getGeometry();
        if(line != null)
            renderEngine.removeFrom3DSpace(line);
        line = new Line(fromCube, toCube, color);
        renderEngine.addTo3DSpace(line);
    }

    public Line getLine()
    {
        return line;
    }
}
