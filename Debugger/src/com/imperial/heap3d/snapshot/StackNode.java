package com.imperial.heap3d.snapshot;

import java.util.Collection;
import java.util.LinkedList;

public class StackNode extends Node {

    private Object value;
    private boolean hasReference;

    public StackNode(String name, Object value) {
        super(name);
        this.value = value;
        hasReference = false;
    }

    public StackNode(String name, Node value) {
        super(name);
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
    public Collection<Object> getPrimitives() {
        Collection<Object> primitives = new LinkedList<>();
        if(!hasReference)
            primitives.add(value);
        return primitives;
    }

    @Override
    public Collection<Node> getReferences() {
        Collection<Node> nodes = new LinkedList<>();
        if(hasReference) {
            nodes.add((Node) value);
        }
        return nodes;
    }
}
