package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Colour;

import java.util.LinkedList;
import java.util.List;

public class StringNode extends Node {

    private String str;

    public StringNode(String name, long id, String str) {
        super(name, id);
        this.str = str;
        this.colour = Colour.AQUA;
    }
    
    public String getString(){
    	return str;
    }

    @Override
    public List<Object> getPrimitives() {
        return new LinkedList<>();
    }

    @Override
    public List<Node> getReferences() {
        return new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof StringNode && getId() == ((Node) o).getId();
    }
}