package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;

public class IterativeSpringBasedLayout extends JungLayout {

    SpringLayout sl;
    @Override
    protected void initializeLayout(DirectedGraph g) {
        sl = new SpringLayout(g);
        layout = sl;
        sl.setSize(new Dimension(1000,1000));
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
