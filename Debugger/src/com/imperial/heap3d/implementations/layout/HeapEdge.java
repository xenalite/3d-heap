package com.imperial.heap3d.implementations.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Shape;

public class HeapEdge {

    private Line line;

    public void connect(Shape from, Shape to, Colour color, IRenderEngine renderEngine) {
        if(line != null)
            renderEngine.removeFrom3DSpace(line);
        line = new Line((Cube) from, (Cube) to, color);
        renderEngine.addTo3DSpace(line);
    }

    public Line getLine()
    {
        return line;
    }
}
