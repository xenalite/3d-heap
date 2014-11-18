package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.DirectedGraph;

import java.awt.*;


public class ISOLayout extends JungLayout{


    ISOMLayout sl;
    @Override
    protected void initializeLayout(DirectedGraph g) {
        sl = new ISOMLayout(g);
        layout = sl;
        sl.setSize(new Dimension(1000,1000));
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