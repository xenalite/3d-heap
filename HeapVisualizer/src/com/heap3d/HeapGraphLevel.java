package com.heap3d;

import EDU.oswego.cs.dl.util.concurrent.Heap;
import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.Graph;
import com.heap3d.layout.GraphImpl;
import com.heap3d.layout.Layout;

import java.awt.geom.Point2D;
import java.util.Collection;

public class HeapGraphLevel extends GraphImpl<HeapNode, String> {

    int id;
    Layout<HeapNode, String> layout;
    private final Colour color;
    public final Colour linecolor;
    public static float levelOffset = 10f;
    protected HeapNode root;


    public HeapGraphLevel(int id)
    {
        this.id = id;
        this.layout = new FRLayout<HeapNode, String>(this);
        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();

        color = new Colour(r, g, b);
        float offset = r+g+b > 2 ? 0.7f : 1.3f;
        linecolor = new Colour(r* offset, g* offset, b* offset);
    }

    @Override
    public boolean addVertex(HeapNode vertex) {
        boolean add = super.addVertex(vertex);
        if (root == null) {
            root = vertex;
        }

        vertex.level = this;

        return add;
    }

    public void buildNode(HeapNode vertex, HeapGraph r)
    {
        if(addVertex(vertex)) {
            float x = getX(vertex);
            float y = getY(vertex);
            float z = getZ(vertex);
            float spacing = 5;
            if (!isRoot(vertex)) {
                if (x < 0) {
                    x -= spacing;
                } else {
                    x += spacing;
                }

                if (y < 0) {
                    y -= spacing;
                } else {
                    y += spacing;
                }
            }
            vertex.buildGeometry(x,y,z,getScale(vertex),color);
            r.addEntityTo3DSpace(vertex.geometry);
        }
    }

    public float getX(HeapNode n)
    {
        return (float)layout.transform(n).getX()/10;
    }
    public float getY(HeapNode n)
    {
        return (float)layout.transform(n).getY()/10;
    }
    public float getZ(HeapNode n)
    {
        return isRoot(n) ? levelOffset * id : ((float) Math.random() * levelOffset) + id*levelOffset;
    }

    public float getScale(HeapNode n)
    {
        return isRoot(n) ? 10 : 1;
    }

    public Boolean isRoot(HeapNode n)
    {
        return n.equals(root);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeapGraphLevel that = (HeapGraphLevel) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
