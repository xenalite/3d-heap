package com.heap3d.implementations.node;

import com.graphics.shapes.Shape;
import com.heap3d.utilities.GeometryUtils;
import com.heap3d.utilities.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

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

    @Override
	public Set<Entry<Object, Object>> getPrimitiveSet() {
		return null;
	}
}
