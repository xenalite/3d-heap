package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;

public class CircularLayout extends JungLayout{

    @Override
    protected void initializeLayout(DirectedGraph g) {
        CircleLayout sl = new CircleLayout(g);
        layout = sl;
        sl.setSize(new Dimension(1000,1000));
        sl.setRadius(500f);
        sl.initialize();
    }

    @Override
    protected void run() {
        //Nothing to do, layout is run in a single step
    }
}


