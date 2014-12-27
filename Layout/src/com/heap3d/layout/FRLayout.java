package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import java.awt.*;

public class FRLayout<V,E> extends JungLayout<V,E> {

    edu.uci.ics.jung.algorithms.layout.FRLayout sl;

    public FRLayout(Graph<V,E> graph)
    {
        sl = new edu.uci.ics.jung.algorithms.layout.FRLayout(graph);
        sl.setInitializer(new RandomLocationTransformer(new Dimension(1000, 1000), 0));
        this.layout = sl;
        setSize(new Dimension(1000,1000));
    }

    public FRLayout(Graph<V,E> graph, float attraction, float repulsion)
    {
        sl = new edu.uci.ics.jung.algorithms.layout.FRLayout(graph);
        sl.setInitializer(new RandomLocationTransformer(new Dimension(1000, 1000), 0));
        sl.setAttractionMultiplier(attraction);
        sl.setRepulsionMultiplier(repulsion);
        this.layout = sl;
        setSize(new Dimension(1000, 1000));
    }

//    @Override
//    protected void initializeLayout(DirectedGraph g, Dimension size) {
//        sl = new edu.uci.ics.jung.algorithms.layout.FRLayout(g);
//        layout = sl;
//        sl.setSize(size);
//        sl.initialize();
//    }
//
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
