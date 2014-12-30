package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.*;

import java.awt.*;
import java.awt.geom.Point2D;


public class ISOLayout<V,E> extends JungLayout<V,E>{

    ISOMLayout sl;

    public ISOLayout(edu.uci.ics.jung.graph.Graph<V,E> graph)
    {
        sl = new ISOMLayout<V,E>(graph);
        this.layout = sl;
        setSize(new Dimension(1000,1000));
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
