package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oskar on 24/11/14.
 */
public class ArrayElemNode extends Node {

    private Object value;
    private boolean hasReference;

    public ArrayElemNode(int index, Object value) {
        super(String.format("[%d]", index), 0);
        this.value = value;
        hasReference = false;
    }

    public ArrayElemNode(int index, Node value) {
        super(String.format("[%d]", index), 0);
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
        if(hasReference)
            nodes.add((Node) value);
        return nodes;
    }

    @Override
    public Shape createShape() {
        return GeometryFactory.createCubeForArrayElemNode();
    }
}