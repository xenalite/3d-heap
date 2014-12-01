package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Colour;

import java.util.*;
import java.util.Map.Entry;

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
    public Object getPrimitives() {
    	String res = "";
    	for(Entry e : _primitives.entrySet()){
    		res += ("(" + e.getKey() + " : " + e.getValue() + ")");
    	}
        return res;
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