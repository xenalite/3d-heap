package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import com.imperial.heap3d.utilities.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayNode extends Node {

    private List<Pair<Node,String>> _references;

    public ArrayNode(long id) {
        super(id);
        this._references = new ArrayList<>();
    }

    public void addElement(Node element, String index) { _references.add(Pair.create(element, index)); }

    @Override
    public List<Object> getPrimitives() {
        List<Object> primitives = new LinkedList<>();
        primitives.add(_references.size());
        return primitives;
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        return _references;
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForArrayNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ArrayNode && getId() == ((Node) o).getId();
    }

}
