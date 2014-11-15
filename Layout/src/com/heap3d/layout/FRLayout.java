package com.heap3d.layout;

import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;

public class FRLayout extends JungLayout {

    edu.uci.ics.jung.algorithms.layout.FRLayout sl;

    @Override
    protected void initializeLayout(DirectedGraph g, Dimension size) {
        sl = new edu.uci.ics.jung.algorithms.layout.FRLayout(g);
        layout = sl;
        sl.setSize(size);
        sl.initialize();
    }

    @Override
    protected void run() {
        int i=0;
        while (i<100 && !sl.done())
        {
            sl.step();
            i++;
        }
    }
}
