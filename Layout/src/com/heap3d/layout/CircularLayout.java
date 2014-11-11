package com.heap3d.layout;

import com.heap3d.Node;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niklas on 11/11/2014.
 */
public class CircularLayout implements LayoutService{
    @Override
    public Map<String, LayoutNode> layout(Map<String, Node> nodes) {
        return layout(nodes, null);
    }

    @Override
    public Map<String, LayoutNode> layout(Map<String, Node> nodes, String rootNodeId) {
        DirectedSparseGraph g = new DirectedSparseGraph();

        //Add all the nodes to the graph as keys
        for(Node n : nodes.values())
        {
            g.addVertex(n);
        }

        //Add all the edges
        int edgeCount = 0;
        for(Node n: nodes.values())
        {
            java.util.List<Node> children = n.getChildren();
            for (int i = 0; i < children.size(); i++) {
                g.addEdge(edgeCount++, n, children.get(i));
            }
        }

        //Setup the Layout Algorithm
        CircleLayout sl = new CircleLayout(g);
        sl.setSize(new Dimension(1000,1000));
        sl.setRadius(500f);
        sl.initialize();

        if(rootNodeId != null)
        {
            if(nodes.containsKey(rootNodeId))
            {
                Node root = nodes.get(rootNodeId);
                sl.setLocation(root, 0.0,0.0);
                sl.lock(root, true);
            }
        }



        Map<String, LayoutNode> map = new HashMap<String, LayoutNode>();

        for(Node n : nodes.values())
        {
            LayoutNode newNode = new LayoutNode(
                    n.getId(),
                    (float)sl.getX(n),
                    (float)sl.getY(n),
                    0.0f
            );
            newNode.getChildren().addAll(n.getChildren());
            map.put(n.getId(), newNode);
        }
        return map;
    }


}


