package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.layout.HeapGraphLevel;
import com.imperial.heap3d.utilities.Pair;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public abstract class Node {

    private long id;
    private HeapGraphLevel level;

    public Node(long id) {
        this.id = id;
    }

    public long getId() { return id; }

    public void setLevel(HeapGraphLevel level) {
        this.level = level;
    }
    public HeapGraphLevel getLevel() {
        return this.level;
    }

    public abstract Object getPrimitives();
    
    public abstract Set<Entry<String, Object>> getPrimitiveSet();

    public abstract List<Pair<Node,String>> getReferences();

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