package com.heap3d;

import com.graphics.RenderEngine;
import com.graphics.shapes.Shape;
import edu.uci.ics.jung.graph.util.EdgeType;


import java.util.*;

public class HeapGraph extends RenderEngine {

    private Set<HeapNode> allNodes = new HashSet<HeapNode>();
    private Set<HeapGraphLevel> levels = new HashSet<HeapGraphLevel>();

    int currentLevel = 0;

    private Random rand;

    public HeapGraph() {
        super("Heap Visualizer!!!", 1280, 720, false);
        super.setBackgroundColour(0f, 0.1f, 0.2f, 1f);
        super.start();

    }

    @Override
    protected void beforeLoop() {
        rand = new Random();
        d = new Date();
    }

    Date d;
    static int level = 0;

    @Override
    protected void inLoop() {
        if (currentLevel < 100 && new Date().after(d)) {

            int numberOfNodes = (int) (Math.random() * 100);
            Map<String, HeapNode> graph = randomGraph(numberOfNodes, 0.1);
            addLevel(graph.values());
            interConnectLevels();
            d = new Date(System.currentTimeMillis()+500);
        }
    }

    private static Map<String, HeapNode> randomGraph(int numberOfNodes, double edgeProbability)
    {
        Map<String, HeapNode> nodes = new HashMap<String, HeapNode>();

        HeapNode root = new HeapNode("0");
        nodes.put("0", root);
        HeapNode prev = root;

        for (int i = 1; i < numberOfNodes/2; i++) {
            String id = i +"";
            HeapNode newNode = new HeapNode(id);
            //Make the graph connected
            newNode.getChildren().add(prev);
            nodes.put(id, newNode);

            prev= newNode;
        }

        for (int i = numberOfNodes/2; i < numberOfNodes; i++) {
            String id = i +"";
            HeapNode newNode = new HeapNode(id);
            nodes.put(id,newNode);
            int randomNodeNumber = (int)(Math.random() * i);
            HeapNode randomNode = nodes.get(""+randomNodeNumber);
            randomNode.getChildren().add(newNode);

        }

        for (int i = 0; i < numberOfNodes; i++) {
            String id = i +"";
            HeapNode n = nodes.get(id);



            int randomNodeNumber = (int)(Math.random() * numberOfNodes);
            double probability = Math.random();
            if(probability <= edgeProbability)
            {
                HeapNode randomNode = nodes.get(""+randomNodeNumber);
                n.getChildren().add(randomNode);
            }
        }


        return nodes;
    }

    private void interConnectLevels()
    {
        int connections = (int)(Math.random() * 5);

//        for (int i = 1; i < connections; i++) {
//            int l1 = (int)(Math.random() * currentLevel);
//            int l2 = (int)(Math.random() * currentLevel);
//            int r1 = (int)(Math.random() * levels.get(l1).size());
//            int r2 = (int)(Math.random() * levels.get(l2).size());
//
//            Cube c1 = cubes.get(r1+""+l1*100);
//            Cube c2 = cubes.get(r2+""+l2*100);
//            c1.addConnection(c2,null);
//        }
    }

    @Override
    protected void afterLoop() {

    }

    public void addLevel(Collection<HeapNode> nodes) {
        HeapGraphLevel levelGraph = new HeapGraphLevel(currentLevel);
        levels.add(levelGraph);

        for (HeapNode n : nodes) {
            allNodes.add(n);
            levelGraph.buildNode(n, this);
        }

        int edgeCount = 0;
        for (HeapNode n : levelGraph.getVertices()) {
            Set<HeapNode> children = n.getChildren();
            for(HeapNode child : children)
            {
                levelGraph.addEdge(""+edgeCount++, n, child, EdgeType.DIRECTED );
            }
        }

        levelGraph.layout.layout();
        for (HeapNode n : nodes) {
            n.updatePosition();
        }

        for (HeapNode n : nodes) {
            Collection<String> outEdges = levelGraph.layout.getGraph().getOutEdges(n);
            for(String edge : outEdges)
            {
                HeapNode child = levelGraph.layout.getGraph().getOpposite(n,edge);
                n.connectTo(child);
            }
        }

        currentLevel++;
    }

    @Override
    public void addEntityTo3DSpace(Shape shape) {
        super.addEntityTo3DSpace(shape);
    }
}
