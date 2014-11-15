package com.heap3d.layout;


import com.heap3d.*;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Running Layout");
//        LayoutService layout = new SpringBasedLayout();
//        LayoutService layout = new IterativeSpringBasedLayout();
//        LayoutService layout = new CircularLayout();
//        LayoutService layout = new ISOLayout();

        Map<String, LayoutNode> g = randomGraph(100, 0.0f);
        Graph<LayoutNode,String> graph = new GraphImpl<LayoutNode, String>();
        //Add all the nodes to the graph as keys
        for(LayoutNode n : g.values())
        {
            graph.addVertex(n);
        }

        //Add all the edges
        int edgeCount = 0;
        for(LayoutNode n: g.values())
        {
            java.util.List<LayoutNode> children = n.getChildren();
            for (int i = 0; i < children.size(); i++) {
                LayoutNode child = new LayoutNode(children.get(i).getId(),0,0,0);
                graph.addEdge(edgeCount++ +"", n, child);
            }
        }

        Layout<LayoutNode,String> layout = new FRLayout<LayoutNode,String>(graph);
        layout.setGraph(graph);

        layout.layout();


//        float maxDistance = 0;
//        for (LayoutNode n : graph.getVertices()) {
//            float x = graph.;
//            float y = n.y();
//            System.out.println(String.format("Node: %s at (%f,%f)", n.getId(), x, y));
//            maxDistance = Math.max(x * x + y * y, maxDistance);
//
//        }
//        System.out.println("Max distance = " + (Math.sqrt(maxDistance)));

    }

    public static Map<String, LayoutNode> randomGraph(int numberOfNodes, double edgeProbability) {
        return  randomGraph(numberOfNodes, edgeProbability, System.currentTimeMillis());
    }

    public static Map<String, LayoutNode> randomGraph(int numberOfNodes, double edgeProbability, long seed)
    {
        Random r = new Random(seed);
        Map<String, LayoutNode> nodes = new HashMap<String, LayoutNode>();

        LayoutNode root = new LayoutNode("0", 0f, 0f, 0f);
        nodes.put("0", root);
        LayoutNode prev = root;

        for (int i = 1; i < numberOfNodes/2; i++) {
            String id = i +"";
            LayoutNode newNode = new LayoutNode(id, 0f, 0f, 0f);
            //Make the graph connected
            newNode.getChildren().add(prev);
            nodes.put(id, newNode);

            prev= newNode;
        }

        for (int i = numberOfNodes/2; i < numberOfNodes; i++) {
            String id = i +"";
            LayoutNode newNode = new LayoutNode(id, 0f, 0f, 0f);
            nodes.put(id,newNode);
            int randomNodeNumber = (int)(r.nextDouble() * i);
            LayoutNode randomNode = nodes.get(""+randomNodeNumber);
            randomNode.getChildren().add(newNode);

        }

        for (int i = 0; i < numberOfNodes; i++) {
            String id = i +"";
            LayoutNode n = nodes.get(id);



            int randomNodeNumber = (int)(r.nextDouble() * numberOfNodes);
            double probability = r.nextDouble();
            if(probability <= edgeProbability)
            {
                LayoutNode randomNode = nodes.get(""+randomNodeNumber);
                n.getChildren().add(randomNode);
            }
        }


        return nodes;
    }
}
