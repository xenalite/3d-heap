package com.heap3d;

import com.graphics.RenderEngine;
import com.graphics.shapes.Colour;
import com.graphics.shapes.Cube;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.LayoutNode;
import com.heap3d.layout.LayoutService;
import com.heap3d.layout.Spatial;

import java.util.*;

public class HeapGraph extends RenderEngine {

    private Map<String,Node> allNodes = new HashMap<String, Node>();
    private List<Map<String,Node>> levels = new ArrayList<Map<String, Node>>(100000);
    private Map<String,Cube> cubes = new HashMap<String, Cube>();

    int currentLevel = 0;

    private Random rand;

    private LayoutService layout;

    public HeapGraph(LayoutService layout)
    {
        super("Test", 1280, 720, false);
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
        if(currentLevel<50) {

            int numberOfNodes = (int)(Math.random() * 100);
            Map<String, com.heap3d.Node> graph = Main.randomGraph(numberOfNodes);
            addLevel(graph.values());
        }
    }

    @Override
    protected void afterLoop() {

    }

    public void addLevel(Collection<Node> nodes)
    {
        System.out.println("Adding"+nodes.size()+" nodes to level "+currentLevel);
        HashMap<String, Node> levelGraph = new HashMap<String, Node>();
        levels.add(levelGraph);

        for(Node n : nodes)
        {
            allNodes.put(n.getId(),n);
            levelGraph.put(n.getId(),n);
        }
        Colour color = new Colour((float)Math.random(), (float)Math.random(), (float)Math.random());
        Map<String, LayoutNode> laidOutNodes = layout.layout(levelGraph);
        if (laidOutNodes != null) {

            for (LayoutNode n : laidOutNodes.values()) {
                float x = n.x() / 10;
                float y = n.y() / 10;
                Cube c = new Cube(x-10, y-50, level, 0, 0, 0, (float)Math.random()*5 +0.2f , color);
                cubes.put(n.getId(), c);
                addEntityTo3DSpace(c);
            }

            for (LayoutNode n : laidOutNodes.values()) {
                List<Node> children = n.getChildren();
                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    Cube c1 = cubes.get(n.getId());
                    Cube c2 = cubes.get(child.getId());
                    c1.addConnection(c2);
                }
            }
        }
        level += 15;

        currentLevel++;
    }

}
