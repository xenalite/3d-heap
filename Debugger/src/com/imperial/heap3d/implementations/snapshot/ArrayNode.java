package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayNode extends Node {

    private List<Node> _elements;

    public ArrayNode(String name, long id) {
        super(name, id);
        this._elements = new ArrayList<>();
    }

    public void addElement(Node element) { _elements.add(element); }

    @Override
    public List<Object> getPrimitives() {
        // TODO -- technically not the length of the array
        List<Object> primitives = new LinkedList<>();
        primitives.add(_elements.size());
        return primitives;
    }

    @Override
    public List<Node> getReferences() {
        return _elements;
    }

    @Override
    public Shape createShape() {
        return GeometryFactory.createCubeForArrayNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ArrayNode && getId() == ((Node) o).getId();
    }

}
