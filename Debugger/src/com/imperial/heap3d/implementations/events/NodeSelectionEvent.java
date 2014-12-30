package com.imperial.heap3d.implementations.events;

import com.imperial.heap3d.implementations.snapshot.Node;

public class NodeSelectionEvent {

    private Node node;

    public NodeSelectionEvent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
