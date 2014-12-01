package com.imperial.heap3d.events;

import com.imperial.heap3d.snapshot.StackNode;

import java.util.Collection;

public class NodeEvent {

    public NodeEvent(Collection<StackNode> nodes) {
        this.nodes = nodes;
    }

    public final Collection<StackNode> nodes;

}
