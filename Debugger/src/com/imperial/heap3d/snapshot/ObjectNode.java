package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;

import java.util.*;

public class ObjectNode extends IdNode {

    private List<Node> _references;
    private Map<String, Object> _primitives;

    public ObjectNode(String name, long id) {
        super(name, id);
        _references = new ArrayList<>();
        _primitives = new HashMap<>();
        this.colour = Colour.ORANGE;
    }

    public void addPrimitive(String name, Object value){
        _primitives.put(name, value);
    }

    public void addReference(Node heapNode){
        _references.add(heapNode);
    }
    
    @Override
    public Collection<Object> getPrimitives() {
        return _primitives.values();
    }

    @Override
    public Collection<Node> getReferences() {
        return _references;
    }
}