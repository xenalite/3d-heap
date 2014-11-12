package com.heap3d.layout;

import com.heap3d.Node;

import java.util.ArrayList;
import java.util.List;

public class LayoutNode implements Node, Spatial {

    private float x;
    private float y;
    private float z;
    private String id;
    List<Node> children = new ArrayList<Node>();

    public LayoutNode(String id, float x, float y, float z)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }


    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float z() {
        return z;
    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LayoutNode) || obj == null)
        {
            return false;
        } else
        {
            return this.getId().equals(((LayoutNode)obj).id);
        }
    }

    @Override
    public String toString() {
        return id + " (" + x+","+y+","+z+")";
    }

    @Override
    public void setX(float v) {
        this.x = v;
    }

    @Override
    public void setY(float v) {
        this.y=v;
    }

    @Override
    public void setZ(float v) {
        this.z=v;
    }
}

