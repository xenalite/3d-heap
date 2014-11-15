package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;


public class ISOLayout extends JungLayout{

    ISOMLayout sl;
    @Override
    protected void initializeLayout(DirectedGraph g, Dimension size) {
        sl = new ISOMLayout(g);
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
