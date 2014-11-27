package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;

import java.util.*;

public class ObjectNode extends Node {

    private List<Node> _references;
    private Map<String, Object> _primitives;

    public ObjectNode(String name, long id) {
        super(name, id);
        _references = new ArrayList<>();
        _primitives = new HashMap<>();
        this.colour = Colour.ORANGE;
    }

    public void addPrimitive(String name, Object value) {
        _primitives.put(name, value);
    }

    public ObjectNode addReference(Node node) {
        _references.add(node);
        return this;
    }

    @Override
    public List<Object> getPrimitives() {
        return new LinkedList<>();
    }

    @Override
    public List<Node> getReferences() {
        return _references;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ObjectNode && getId() == ((Node) o).getId();
    }
}