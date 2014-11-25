package com.imperial.heap3d.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.imperial.heap3d.snapshot.Node;

public class HeapEdge {

    private Line line;

    public void connect(Node from, Node to, Colour color, HeapGraph graph) {
        // TODO -- encapsulate geometry
        line = new Line((Cube)from.getGeometry(), (Cube)to.getGeometry(), color);
        graph.addShapeTo3DSpace(line);
    }
}
