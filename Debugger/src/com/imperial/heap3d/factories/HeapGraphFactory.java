package com.imperial.heap3d.factories;

import com.imperial.heap3d.layout.HeapGraph;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by oskar on 23/11/14.
 */
public class HeapGraphFactory {

    private final HeapGraph _heapGraph;

    public HeapGraphFactory(Canvas canvas) {
        _heapGraph = new HeapGraph(canvas, new LinkedList<>());
    }

    public HeapGraph create() {
        return _heapGraph;
    }
}
