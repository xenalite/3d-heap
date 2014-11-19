package com.imperial.heap3d.snapshot;

import java.util.HashSet;
import java.util.Set;

public class Snapshot {

    private Set<StackNode> stackNodes;

    public Snapshot() {
        this.stackNodes = new HashSet<>();
    }

    public Set<StackNode> getStackNodes() {
        return stackNodes;
    }

    public void addStackNode(StackNode stackNode) {
        stackNodes.add(stackNode);
    }

    public void clear() {
        stackNodes.clear();
    }
}
