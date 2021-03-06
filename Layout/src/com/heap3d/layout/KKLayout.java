package com.heap3d.layout;

import java.awt.*;

public class KKLayout<V,E> extends JungLayout<V,E> {

    edu.uci.ics.jung.algorithms.layout.KKLayout<V,E> sl;
    public KKLayout(edu.uci.ics.jung.graph.Graph<V,E> graph)
    {
        sl = new edu.uci.ics.jung.algorithms.layout.KKLayout<V,E>(graph);
        this.layout = sl;
        setSize(new Dimension(1000,1000));
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
