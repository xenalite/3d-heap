package com.heap3d;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Line;
import com.graphics.shapes.Shape;
import com.heap3d.layout.LayoutNode;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HeapNode {

    String id;

    public HeapNode(String id) {
        this.id = id;
    }

    //The shape associated with the the node
    Shape geometry;
    // Where the heap data is stored, the type will likely change
    Object heapData;
    // Which stack frame is the object in
    HeapGraphLevel level;
    Map<String,Line> lines;
    Set<HeapNode> children = new HashSet<HeapNode>();


    public void buildGeometry(float x, float y, float z, float scale, Colour c)
    {
        this.geometry = new Cube(x,y,z,0,0,0,scale,c);
    }

    public void updatePosition()
    {
        if(geometry != null)
        {
            geometry.getEntity().setPosition(new Vector3f(level.getX(this),level.getY(this),level.getZ(this)));
        }
    }

    public void connectTo(HeapNode n)
    {
        ((Cube)geometry).addConnection((Cube) n.geometry, level.linecolor);
    }

    public Set<HeapNode> getChildren()
    {
        return children;
    }

}
