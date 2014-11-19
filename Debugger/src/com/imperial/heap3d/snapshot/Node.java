package com.imperial.heap3d.snapshot;

import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

public abstract class Node {

    private String name;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
