package com.heap3d.layout;

import com.heap3d.Node;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.project.api.Workspace;
import org.gephi.project.impl.ProjectImpl;
import org.gephi.workspace.impl.WorkspaceImpl;
import org.openide.util.Lookup;

import java.util.HashMap;
import java.util.Map;

public class SpringBasedLayout implements LayoutService {
    @Override
    public Map<String, LayoutNode> layout(Map<String, Node> nodes) {

        Workspace ws = new WorkspaceImpl(new ProjectImpl());
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel(ws);
        DirectedGraph graph = graphModel.getDirectedGraph();

        //Add all the nodes
        GraphFactory factory = graphModel.factory();
        for (Node hn : nodes.values()) {
            org.gephi.graph.api.Node node = factory.newNode(hn.getId());
            graph.addNode(node);
        }

        //Add all the edges between nodes
        for (Node hn : nodes.values()) {
            for (Node child : hn.getChildren()) {
                org.gephi.graph.api.Node childGraphNode = graph.getNode(child.getId());
                org.gephi.graph.api.Node sourceGraphNode = graph.getNode(hn.getId());
                graph.addEdge(sourceGraphNode, childGraphNode);
            }
        }

        FruchtermanReingold layout = new FruchtermanReingold(null);

//        YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
        layout.setGraphModel(graphModel);
        layout.initAlgo();
        layout.resetPropertiesValues();
//        layout.setOptimalDistance(20f);

        for (int i = 0; i < 100 && layout.canAlgo(); i++) {
//            System.out.println("Laying Out ..");
            layout.goAlgo();
        }
        layout.endAlgo();

        Map<String, LayoutNode> map = new HashMap<String, LayoutNode>();
        for(org.gephi.graph.api.Node n : graph.getNodes())
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
}
