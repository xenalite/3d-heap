package com.heap3d.layout;

import com.heap3d.Node;
import org.gephi.graph.api.*;
import org.gephi.graph.spi.LayoutData;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.project.api.Workspace;
import org.gephi.project.impl.ProjectImpl;
import org.gephi.workspace.impl.WorkspaceImpl;
import org.openide.util.Lookup;

import java.util.HashMap;
import java.util.Map;

public class SpringBasedLayout implements LayoutService {
    @Override
    public Map<String, LayoutNode> layout(Map<String, Node> nodes) {
        return layout(nodes, null);
    }

    @Override
    public Map<String, LayoutNode> layout(Map<String, Node> nodes, String rootNodeId) {

        Workspace ws = new WorkspaceImpl(new ProjectImpl());
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel(ws);
        DirectedGraph graph = graphModel.getDirectedGraph();

        //Add all the nodes
        GraphFactory factory = graphModel.factory();
        for (Node hn : nodes.values()) {
            org.gephi.graph.api.Node node = factory.newNode(hn.getId());
            node.getNodeData().setX(0f);
            node.getNodeData().setY(0f);
            graph.addNode(node);
        }

        if(rootNodeId != null)
        {
            org.gephi.graph.api.Node root = graph.getNode(rootNodeId);
            NodeData nd = root.getNodeData();
            nd.setFixed(true);
            nd.setX(0.0f);
            nd.setY(0.0f);
        }

        //Add all the edges between nodes
        for (Node hn : nodes.values()) {
            for (Node child : hn.getChildren()) {
                org.gephi.graph.api.Node childGraphNode = graph.getNode(child.getId());
                org.gephi.graph.api.Node sourceGraphNode = graph.getNode(hn.getId());
                graph.addEdge(sourceGraphNode, childGraphNode);
            }
        }

        LayoutExtender layout = new LayoutExtender();
        layout.setGraphModel(graphModel);
        //layout.resetPropertiesValues();
        layout.initAlgo();

        for (int i = 0; i < 100 && layout.canAlgo(); i++) {
            layout.goAlgo();
        }
        layout.endAlgo();

        Map<String, LayoutNode> map = new HashMap<String, LayoutNode>();

        NodeIterable vn = layout.getGraph().getNodes();

        NodeIterable gn = graph.getNodes();
        NodeIterable gmn = graphModel.getHierarchicalDirectedGraph().getNodes();

        for(org.gephi.graph.api.Node n : vn)
        {
            LayoutNode newNode = new LayoutNode(
                    n.getNodeData().getId(),
                    n.getNodeData().x(),
                    n.getNodeData().y(),
                    n.getNodeData().z()
            );
            newNode.getChildren().addAll(nodes.get(newNode.getId()).getChildren());
            map.put(n.getNodeData().getId(), newNode);
        }

        return map;
    }

    public class LayoutExtender extends FruchtermanReingold
    {
        public LayoutExtender()
        {
            super(null);
        }

        public Graph getGraph()
        {
            return this.graph;
        }
    }
}
