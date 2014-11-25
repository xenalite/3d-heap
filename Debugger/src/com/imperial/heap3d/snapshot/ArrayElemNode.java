package com.imperial.heap3d.snapshot;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by oskar on 24/11/14.
 */
public class ArrayElemNode extends Node {

    private Object value;
    private boolean hasReference;

    public ArrayElemNode(int index, Object value) {
        super(String.format("[%d]", index));
        this.value = value;
        hasReference = false;
    }

    public ArrayElemNode(int index, Node value) {
        super(String.format("[%d]", index));
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
        if(hasReference)
            nodes.add((Node) value);
        return nodes;
    }
}