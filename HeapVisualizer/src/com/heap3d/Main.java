package com.heap3d;

import com.heap3d.layout.CircularLayout;
import com.heap3d.layout.FRLayout;
import com.heap3d.layout.LayoutNode;
import com.heap3d.layout.LayoutService;
import org.gephi.graph.api.*;
import org.gephi.io.generator.plugin.RandomGraph;
import org.gephi.io.importer.api.ContainerFactory;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


import java.util.HashMap;
import java.util.Map;

public class Main  {

    public static void main(String[] args)
    {
        HeapGraph h = new HeapGraph(new CircularLayout());
    }



    public static Map<String, Node> randomGraph(int numberOfNodes) {
        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Generate a new random graph into a container
        org.gephi.io.importer.api.Container container = Lookup.getDefault().lookup(ContainerFactory.class).newContainer();
        RandomGraph randomGraph = new RandomGraph();
        randomGraph.setNumberOfNodes(numberOfNodes);
        randomGraph.setWiringProbability(0.05);
        randomGraph.generate(container.getLoader());

        //Append container to graph structure
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        importController.process(container, new DefaultProcessor(), workspace);

        //See if graph is well imported
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        DirectedGraph graph = graphModel.getDirectedGraph();

        Map<String, com.heap3d.Node> nodes = new HashMap<String, Node>();
        NodeIterable ni = graph.getNodes();
        for (org.gephi.graph.api.Node n : ni) {
            LayoutNode newNode = new LayoutNode(
                    n.getNodeData().getId(), 0f, 0f, 0f);
            nodes.put(n.getNodeData().getId(), newNode);
        }

        for (Edge e : graph.getEdges()) {
            com.heap3d.Node source = nodes.get(e.getSource().getNodeData().getId());
            com.heap3d.Node target = nodes.get(e.getTarget().getNodeData().getId());
            source.getChildren().add(target);
        }

        return nodes;
    }
}
