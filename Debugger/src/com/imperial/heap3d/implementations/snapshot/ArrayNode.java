package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;

import java.util.*;

public class ArrayNode extends Node {
    private List<Node> _references;
    private Map<Integer, Object> _primitives;


    public ArrayNode(String name, long id) {
        super(name, id);
        _references = new ArrayList<>();
        _primitives = new HashMap<>();
    }

    public void addPrimitive(int index, Object value) {
        _primitives.put(index, value);
    }

    @Override
    public Object getPrimitives() {
        String res = "";
        for(Map.Entry e : _primitives.entrySet()){
            res += ("[" + e.getKey() + "] = " + e.getValue()+"\n");
        }
        return res;
    }

    public void addReference(Node node) {
        _references.add(node);
    }

    @Override
    public List<Node> getReferences() {
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
