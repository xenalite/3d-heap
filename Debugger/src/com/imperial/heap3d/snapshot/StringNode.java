package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;

import java.util.Collection;
import java.util.LinkedList;

public class StringNode extends IdNode {

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
    public Collection<Object> getPrimitives() {
        return new LinkedList<>();
    }

    @Override
    public Collection<Node> getReferences() {
        return new LinkedList<>();
    }
}
