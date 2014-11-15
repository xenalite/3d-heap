package com.heap3d.layout;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Graph {

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
