package com.imperial.heap3d.snapshot;

public class StringNode extends Node {

    private String str;
    private long id;

    public StringNode(String name, long id, String str) {
        super(name);
        this.id = id;
        this.str = str;
    }
}
