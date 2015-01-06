package com.heap3d.implementations.events;

import com.heap3d.implementations.node.Node;

public class NodeSelectionEvent {

    private Node node;

    public NodeSelectionEvent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
