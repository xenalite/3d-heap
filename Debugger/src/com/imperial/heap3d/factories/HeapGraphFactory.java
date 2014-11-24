package com.imperial.heap3d.factories;

import com.imperial.heap3d.layout.HeapGraph;
import com.imperial.heap3d.snapshot.StackNode;

import java.awt.*;
import java.util.Set;

/**
 * Created by oskar on 23/11/14.
 */
public class HeapGraphFactory {

    private final Canvas _canvas;

    public HeapGraphFactory(Canvas canvas) {
        _canvas = canvas;
    }

    public HeapGraph create(Set<StackNode> stackNodes) {
        return new HeapGraph(_canvas, stackNodes);
    }
}
