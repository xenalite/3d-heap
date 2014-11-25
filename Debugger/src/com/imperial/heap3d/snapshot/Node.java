package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.layout.HeapGraphLevel;

import java.util.Collection;

public abstract class Node {

    private String name;

    private HeapGraphLevel level;
    private Shape geometry;
    protected Colour colour;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLevel(HeapGraphLevel level) {
        this.level = level;
    }

    public abstract Collection<Object> getPrimitives();

    public abstract Collection<Node> getReferences();

    //region Geometry
    public void buildGeometry(float x, float y, float z, float scale, Colour c) {
        this.geometry = new Cube(x, y, z, 0, 0, 0, scale, c);
    }

    public void buildGeometry(float x, float y, float z, float scale) {
        this.geometry = new Cube(x, y, z, 0, 0, 0, scale, colour);
    }

    public Shape getGeometry() {
        return geometry;
    }

    public void updatePosition() {
        if (geometry != null)
            geometry.setPosition(level.getX(this), level.getY(this), level.getZ(this));
    }
    //endregion
}
