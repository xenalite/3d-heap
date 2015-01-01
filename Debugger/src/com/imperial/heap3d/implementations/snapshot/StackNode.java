package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import com.imperial.heap3d.utilities.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StackNode extends Node {

    private String _name;
    private Object _value;
    private boolean _hasReference;

    public StackNode(String name, long id) {
        super(id);
        _name = name;
    }

    public StackNode(String name, Object value) {
        super(value == null ? 0 : value.hashCode());
        _value = value;
        _name = name;
        _hasReference = false;
    }

    public StackNode(String name, Pair<Node,String> value) {
        super(value.first.getId());
        _value = value;
        _name = name;
        _hasReference = true;
    }
    
    public boolean hasReference(){
    	return _hasReference;
    }
    
    @Override
    public List<Object> getPrimitives() {
        List<Object> primitives = new LinkedList<>();
        if(!_hasReference)
            primitives.add(_value);
        return primitives;
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        List<Pair<Node, String>> nodes = new LinkedList<>();
        if(_hasReference) {
            nodes.add((Pair<Node,String>) _value);
        }
        return nodes;
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForStackNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof StackNode &&
                getId() == ((StackNode) o).getId() &&
                Objects.equals(getName(), ((StackNode) o).getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ getName().hashCode();
    }

    public String getName() {
        return _name;
    }
}