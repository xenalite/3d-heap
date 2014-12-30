package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import com.imperial.heap3d.utilities.Pair;

import java.util.LinkedList;
import java.util.List;

public class StringNode extends Node {

    private String str;

    public StringNode(long id, String str) {
        super(id);
        this.str = str;
    }
    
    public String getString(){
    	return str;
    }

    @Override
    public List<Object> getPrimitives() {
        return new LinkedList<>();
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        return new LinkedList<>();
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForStringNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof StringNode && getId() == ((Node) o).getId();
    }
}
