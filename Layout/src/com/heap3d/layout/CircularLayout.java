package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;

public class CircularLayout extends JungLayout{

    @Override
    protected void initializeLayout(DirectedGraph g, Dimension size) {
        CircleLayout sl = new CircleLayout(g);
        layout = sl;
        sl.setSize(size);
        sl.setRadius(+size.getHeight()/2.01f);
        sl.initialize();
    }

    @Override
    protected void run() {
        //Nothing to do, layout is run in a single step
    }
}


