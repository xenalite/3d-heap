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


}

