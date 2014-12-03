package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.layout.HeapGraphLevel;
import com.imperial.heap3d.implementations.utilities.ColorConverter;

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
        colour = ColorConverter.randomColour();
    }

    public String getName() {
        return name;
    }

    public long getId() { return id; }

    public void setLevel(HeapGraphLevel level) {
        this.level = level;
    }
    public HeapGraphLevel getLevel() {
        return this.level;
    }

    public abstract Object getPrimitives();

    public abstract List<Node> getReferences();

    //region Geometry
    public void buildGeometry(float x, float y, float z, float scale) {
        System.out.println("Building cube: "+this.getName());
        this.geometry = new Cube(0, 0, 0, 0, 0, 0, scale, colour);
    }

    public Shape getGeometry() {
        return geometry;
    }

    public void updatePosition() {
        if (geometry != null)
        {
            float x = level.getX(this);
            float y = level.getY(this);
            float z = level.getZ(this);
            //System.out.println(String.format("Updating to: %f %f %f", x,y,z));
            geometry.setPosition(x, y, z);
        }
        else
        {
            System.err.println("Can't update non existant geometry");
        }
    }

    public Colour getColour() {
        return colour;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        return this == o || o.getClass().equals(getClass()) && id == ((Node) o).id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >> 32));
    }
}