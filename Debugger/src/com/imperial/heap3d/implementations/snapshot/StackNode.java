package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;

import java.util.LinkedList;
import java.util.List;

public class StackNode extends Node {

    private Object value;
    private boolean hasReference;

    public StackNode(String name, Object value) {
        super(name, value == null ? 0 : value.hashCode());
        this.value = value;
        hasReference = false;
    }

    public StackNode(String name, Node value) {
        super(name, value.getId());
        this.value = value;
        hasReference = true;
    }
    
    public Object getValue(){
    	return value;
    }
    
    public boolean hasReference(){
    	return hasReference;
    }
    
    @Override
    public List<Object> getPrimitives() {
        List<Object> primitives = new LinkedList<>();
        if(!hasReference)
            primitives.add(value);
        return primitives;
    }

    @Override
    public List<Node> getReferences() {
        List<Node> nodes = new LinkedList<>();
        if(hasReference) {
            nodes.add((Node) value);
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
                getName().equals(((StackNode) o).getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ getName().hashCode();
    }
}