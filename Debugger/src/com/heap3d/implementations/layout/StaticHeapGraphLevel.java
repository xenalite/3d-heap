package com.heap3d.implementations.layout;

import com.heap3d.implementations.node.Node;
import com.heap3d.implementations.node.StackNode;

import javax.vecmath.Vector3f;
import java.util.Vector;

/**
 * Created by costica on 1/8/2015.
 */
public class StaticHeapGraphLevel extends HeapGraphLevel {
    public StaticHeapGraphLevel(int id) {
        super(id);
    }

    @Override
    public boolean addVertex(Node vertex) {
        if(_graph.addVertex(vertex)) {
            vertex.setLevel(this);
            if (vertex instanceof StackNode) {
                root = (StackNode) vertex;
            }
            return true;
        }
        return false;
    }

    @Override
    public Vector3f getPosition(Node n)
    {
        float x = (float) _layout.transform(n).getX();
        float y = -15;
        float z = (float) _layout.transform(n).getY();

        Vector3f pos = new Vector3f(x,y,z);

        Vector3f dir = new Vector3f(x,0,z);
        if(dir.length() == 0)
            return pos;
        dir.normalize();
        dir.scale(SPACING);
        pos.add(dir);
        return pos;
    }
}
