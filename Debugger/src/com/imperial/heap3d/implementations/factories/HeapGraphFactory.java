package com.imperial.heap3d.implementations.factories;

import com.imperial.heap3d.implementations.layout.HeapGraph;

/**
 * Created by oskar on 23/11/14.
 */
public class HeapGraphFactory {

    private final HeapGraph _heapGraph;

    public HeapGraphFactory(HeapGraph heapGraph) {
        _heapGraph = heapGraph;
    }

    public HeapGraph create() {
        return _heapGraph;
    }
}
