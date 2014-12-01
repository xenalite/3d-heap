package com.imperial.heap3d.implementations.events;

import com.imperial.heap3d.implementations.snapshot.StackNode;

import java.util.Collection;

public class NodeEvent {

    public NodeEvent(Collection<StackNode> nodes) {
        this.nodes = nodes;
    }

    public final Collection<StackNode> nodes;

}
