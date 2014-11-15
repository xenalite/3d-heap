package com.heap3d;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.heap3d.layout.LayoutNode;
import com.heap3d.layout.LayoutService;


import java.util.*;

public class HeapGraph extends RenderEngine {

    private Map<String, LayoutNode> allNodes = new HashMap<String, LayoutNode>();
    private List<Map<String, LayoutNode>> levels = new ArrayList<Map<String, LayoutNode>>(100000);
    private Map<String, Cube> cubes = new HashMap<String, Cube>();

    int currentLevel = 0;

    private Random rand;

    private LayoutService layout;

    public HeapGraph(LayoutService layout) {
        super("Heap Visualizer!!!", 1280, 720, false);
        this.layout = layout;
        super.setBackgroundColour(0f, 0.1f, 0.2f, 1f);
        super.start();

    }

    @Override
    protected void beforeLoop() {
        rand = new Random();
    }

    static float level = 0;

    @Override
    protected void inLoop() {
        if (currentLevel < 50) {

            int numberOfNodes = (int) (Math.random() * 100);
            Map<String, LayoutNode> graph = randomGraph(numberOfNodes, 0.1);
            addLevel(graph.values());
            interConnectLevels();
        }
    }

    private static Map<String, LayoutNode> randomGraph(int numberOfNodes, double edgeProbability)
    {
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
            int randomNodeNumber = (int)(Math.random() * i);
            LayoutNode randomNode = nodes.get(""+randomNodeNumber);
            randomNode.getChildren().add(newNode);

        }

        for (int i = 0; i < numberOfNodes; i++) {
            String id = i +"";
            LayoutNode n = nodes.get(id);



            int randomNodeNumber = (int)(Math.random() * numberOfNodes);
            double probability = Math.random();
            if(probability <= edgeProbability)
            {
                LayoutNode randomNode = nodes.get(""+randomNodeNumber);
                n.getChildren().add(randomNode);
            }
        }


        return nodes;
    }

    private void interConnectLevels()
    {
        int connections = (int)(Math.random() * 5);

        for (int i = 1; i < connections; i++) {
            int l1 = (int)(Math.random() * currentLevel);
            int l2 = (int)(Math.random() * currentLevel);
            int r1 = (int)(Math.random() * levels.get(l1).size());
            int r2 = (int)(Math.random() * levels.get(l2).size());

            Cube c1 = cubes.get(r1+""+l1*100);
            Cube c2 = cubes.get(r2+""+l2*100);
            c1.addConnection(c2,null);
        }
    }

    @Override
    protected void afterLoop() {

    }

    public void addLevel(Collection<LayoutNode> nodes) {
        HashMap<String, LayoutNode> levelGraph = new HashMap<String, LayoutNode>();
        levels.add(levelGraph);

        for (LayoutNode n : nodes) {
            allNodes.put(n.getId(), n);
            levelGraph.put(n.getId(), n);
        }
        float r = (float) Math.random();
        float g = (float) Math.random();
        float b = (float) Math.random();

        Colour color = new Colour(r, g, b);
        float offset = r+g+b > 2 ? 0.7f : 1.3f;
        Colour linecolor = new Colour(r*offset, g*offset, b*offset);

        layout.layout(levelGraph);
        for (LayoutNode n : levelGraph.values()) {
            float x = n.x() / 10;
            float y = n.y() / 10;
            Cube c = new Cube(x - 10, y - 50, level, 0, 0, 0, 1, color);
            cubes.put(n.getId()+currentLevel*100, c);
            addEntityTo3DSpace(c);
        }

        for (LayoutNode n : levelGraph.values()) {
            List<Node> children = n.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                Cube c1 = cubes.get(n.getId()+currentLevel*100);
                Cube c2 = cubes.get(child.getId()+currentLevel*100);
                c1.addConnection(c2,linecolor);
            }
        }

        level += 15;

        currentLevel++;
    }


}
