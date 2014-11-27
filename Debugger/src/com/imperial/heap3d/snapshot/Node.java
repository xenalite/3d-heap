package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.layout.HeapGraphLevel;

import java.util.List;

public abstract class Node {

    private String name;
    private long id;

    private HeapGraphLevel level;
    private Shape geometry;
    protected Colour colour;

    public Node(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() { return id; }

    public void setLevel(HeapGraphLevel level) {
        this.level = level;
    }

    public abstract List<Object> getPrimitives();

    public abstract List<Node> getReferences();

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

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Node && id == ((Node) o).id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >> 32));
    }
}