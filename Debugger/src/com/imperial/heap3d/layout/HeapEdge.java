package com.imperial.heap3d.layout;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.imperial.heap3d.snapshot.Node;

public class HeapEdge {

	private long id;
    private Line line;
    //TODO not sure these are necesary since the layout graph stores this information
    private Node from;
    private Node to;

    public HeapEdge(long id){
        this.id = id;
    }

    public void connect(Node from, Node to, Colour color, HeapGraph graph){
        this.from = from;
        this.to = to;
        line = new Line((Cube)from.getGeometry(), (Cube)to.getGeometry(), new Colour(1, 1, 1)); //TODO geometry should be encapsulated better
        graph.addShapeTo3DSpace(line);
    }
	
}
