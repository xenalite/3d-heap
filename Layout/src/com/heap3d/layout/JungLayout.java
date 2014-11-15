package com.heap3d.layout;

import com.heap3d.Node;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class JungLayout implements LayoutService{

    protected AbstractLayout layout;

    @Override
    public <T extends Spatial & Node> void layout(Map<String, T> nodes) {
        layout(nodes, null);
    }

    @Override
    public <T extends Spatial & Node> void layout(Map<String, T> nodes, String rootNodeId) {
        DirectedSparseGraph g = new DirectedSparseGraph();

        //Add all the nodes to the graph as keys
        for(T n : nodes.values())
        {
            g.addVertex(n);
        }

        //Add all the edges
        int edgeCount = 0;
        for(T n: nodes.values())
        {
            java.util.List<Node> children = n.getChildren();
            for (int i = 0; i < children.size(); i++) {
                g.addEdge(edgeCount++, n, children.get(i));
            }
        }

        //Initialize the graph
        initializeLayout(g,size);

        double midx = size.width/2.0;
        double midy = size.height/2.0;

        //Freeze the root node at 0,0
        if(rootNodeId != null)
        {
            if(nodes.containsKey(rootNodeId))
            {
                T root = nodes.get(rootNodeId);

                layout.setLocation(root, midx, midy);
                layout.lock(root, true);
            }
        }

        //run the layout
        run();

        for(T n : nodes.values())
        {
            n.setX((float)(layout.getX(n)-midx));
            n.setY((float)(layout.getY(n)-midy));
        }
    }


    Dimension size = new Dimension(1000,1000);
    @Override
    public void setSize(int size) {
        this.size = new Dimension(size,size);
    }

    protected abstract void initializeLayout(DirectedGraph g, Dimension size);
    protected abstract void run();

}
