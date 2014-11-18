package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.*;

import java.awt.*;

public class CircularLayout <V,E> extends JungLayout<V,E>{

    CircleLayout<V,E> sl;

    public CircularLayout(edu.uci.ics.jung.graph.Graph<V,E> graph)
    {
        sl = new CircleLayout<V,E>(graph);
        this.layout = sl;
        setSize(new Dimension(1000,1000));
        //sl.setRadius(500);

    }

//    @Override
//    protected void initializeLayout(DirectedGraph g, Dimension size) {
//        CircleLayout sl = new CircleLayout(g);
//        layout = sl;
//        sl.setSize(size);
//        sl.setRadius(+size.getHeight()/2.01f);
//        sl.initialize();
//    }

    @Override
    protected void run() {
        //Nothing to do, layout is run in a single step
    }
}


