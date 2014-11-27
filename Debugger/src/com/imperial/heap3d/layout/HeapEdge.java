package com.imperial.heap3d.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.imperial.heap3d.snapshot.Node;

public class HeapEdge {

    public HeapEdge()
    {

    }

    public HeapEdge(Node from, Node to)
    {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }


    private Node from;
    private Node to;

    private Line line;

    public void connect(Colour color, HeapGraph graph) {
        line = new Line((Cube)from.getGeometry(), (Cube)to.getGeometry(), color);
        graph.addShapeTo3DSpace(line);
    }

    public void connect(Node from, Node to, Colour color, HeapGraph graph) {
        this.from = from;
        this.to = to;
        connect(color,graph);
    }

    public Line getLine()
    {
        return line;
    }
}
