package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;

public class IterativeSpringBasedLayout extends JungLayout {

    SpringLayout sl;
    @Override
    protected void initializeLayout(DirectedGraph g, Dimension size) {
        sl = new SpringLayout(g);
        layout = sl;
        sl.setSize(size);
        sl.initialize();
    }

    @Override
    protected void run() {
        int i=0;
        while (i<100)
        {
            sl.step();
            i++;
        }
    }
}
