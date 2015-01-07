package com.heap3d.implementations.events;

import com.heap3d.implementations.node.StackNode;

import java.util.Collection;

public class NodeEvent {

    public NodeEvent(Collection<StackNode> nodes) {
        this.nodes = nodes;
    }

    public final Collection<StackNode> nodes;

}
