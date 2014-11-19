package com.imperial.heap3d.snapshot;

public class StringNode extends IDNode {

    private String str;

    public StringNode(String name, long id, String str) {
        super(name, id);
        this.str = str;
    }
    
    public String getString(){
    	return str;
    }
}
