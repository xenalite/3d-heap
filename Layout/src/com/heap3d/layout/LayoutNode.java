package com.heap3d.layout;

import java.util.ArrayList;
import java.util.List;

class LayoutNode  {

    private float x;
    private float y;
    private float z;
    private String id;
    List<LayoutNode> children = new ArrayList<LayoutNode>();

    public LayoutNode(String id, float x, float y, float z)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getId() {
        return id;
    }

    public List<LayoutNode> getChildren() {
        return children;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
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

}

