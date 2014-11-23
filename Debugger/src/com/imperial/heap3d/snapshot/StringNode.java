package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;

public class StringNode extends IDNode {

    private String str;

    public StringNode(String name, long id, String str) {
        super(name, id);
        this.str = str;
        this.colour = Colour.AQUA;
    }
    
    public String getString(){
    	return str;
    }
}
