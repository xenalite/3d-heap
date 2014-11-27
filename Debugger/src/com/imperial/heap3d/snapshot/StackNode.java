package com.imperial.heap3d.snapshot;

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
}