package com.imperial.heap3d.implementations.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.layout.HeapGraph;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by oskar on 23/11/14.
 */
public class HeapGraphFactory {

    private final HeapGraph _heapGraph;

    public HeapGraphFactory(Canvas canvas, EventBus eventBus) {
        _heapGraph = new HeapGraph(canvas, new LinkedList<>(), eventBus);
    }
    
    public HeapGraphFactory() {
        _heapGraph = new HeapGraph(new LinkedList<>());
    }

    public HeapGraph create() {
        return _heapGraph;
    }
}
