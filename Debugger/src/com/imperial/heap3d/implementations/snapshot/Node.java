package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.layout.HeapGraphLevel;

import java.util.List;

public abstract class Node {

    private String name;
    private long id;
    private HeapGraphLevel level;

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
    public HeapGraphLevel getLevel() {
        return this.level;
    }

    public abstract Object getPrimitives();

    public abstract List<Node> getReferences();

    public abstract Shape createShape();

    @Override
    public boolean equals(Object o) {
        return this == o || o.getClass().equals(getClass()) && id == ((Node) o).id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >> 32));
    }
}