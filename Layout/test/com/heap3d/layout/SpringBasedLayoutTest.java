package com.heap3d.layout;

import com.heap3d.Node;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Lookup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SpringBasedLayoutTest {

    private static Boolean areNodesEqual(Node originalNode, Node newNode) {
        Boolean equal = true;
        equal &= originalNode.getId().equals(newNode.getId());

        List<Node> oChildren = originalNode.getChildren();
        List<Node> nChildren = newNode.getChildren();
        if (oChildren.size() == nChildren.size()) {
            for (int i = 0; i < originalNode.getChildren().size(); i++) {
                equal &= oChildren.get(i).getId().equals(nChildren.get(i).getId());
            }
        } else
        {
            return false;
        }

        return equal;
    }

//    @Test
//    public void testLayoutEmptyPass() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Map<String, LayoutNode> layout = sl.layout(graph);
//
//        assertEquals(layout.size(), graph.size());
//    }
//
//    @Test
//    public void testLayoutSinglePass() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Node n = new LayoutNode("id", 0, 0, 0);
//        graph.put(n.getId(), n);
//        Map<String, LayoutNode> layout = sl.layout(graph);
//
//        assertTrue(layout.containsKey(n.getId()));
//        Node layoutNode = layout.get(n.getId());
//        assertTrue(areNodesEqual(n, layoutNode));
//    }
//
//    @Test
//    public void testLayoutPairNonDestructivePass() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Node a = new LayoutNode("a", 0, 0, 0);
//        Node b = new LayoutNode("b", 0, 0, 0);
//        a.getChildren().add(b);
//        b.getChildren().add(a);
//        graph.put(a.getId(), a);
//        graph.put(b.getId(), b);
//        Map<String, LayoutNode> layout = sl.layout(graph);
//
//        for (Node n : graph.values()) {
//            assertTrue(layout.containsKey(n.getId()));
//            Node layoutNode = layout.get(n.getId());
//            assertTrue(areNodesEqual(n, layoutNode));
//        }
//    }
//
//    @Test
//    public void testLayoutRootNodeFixedPosition() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Node n = new LayoutNode("id", 10, 10, 10);
//        graph.put(n.getId(), n);
//        Map<String, LayoutNode> layout = sl.layout(graph, "id");
//
//        LayoutNode layoutNode = layout.get(n.getId());
//        assertEquals(0.0f, layoutNode.x(), 0.0f);
//        assertEquals(0.0f, layoutNode.y(), 0.0f);
//    }
//
//    @Test
//    public void testLayoutPairNonDestructiveFail() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Node a = new LayoutNode("a", 0, 0, 0);
//        Node b = new LayoutNode("b", 0, 0, 0);
//        a.getChildren().add(b);
//        b.getChildren().add(a);
//        graph.put(a.getId(), a);
//        graph.put(b.getId(), b);
//        Map<String, LayoutNode> layout = sl.layout(graph);
//
//        for (Node n : graph.values()) {
//            assertTrue(layout.containsKey(n.getId()));
//            Node layoutNode = layout.get(n.getId());
//            //Remove the children
//            layoutNode.getChildren().clear();
//            //this should fail
//            assertFalse(areNodesEqual(n, layoutNode));
//        }
//    }

//    @Test
//    public void testLayoutAdditionalEdgesFail() throws Exception {
//
//        SpringBasedLayout sl = new SpringBasedLayout();
//
//        Map<String, Node> graph = new HashMap<String, Node>();
//        Node a = new LayoutNode("a", 0, 0, 0);
//        Node b = new LayoutNode("b", 0, 0, 0);
//        a.getChildren().add(b);
//        b.getChildren().add(a);
//        graph.put(a.getId(), a);
//        graph.put(b.getId(), b);
//        Map<String, LayoutNode> layout = sl.layout(graph);
//
//        for (Node n : graph.values()) {
//            assertTrue(layout.containsKey(n.getId()));
//            Node layoutNode = layout.get(n.getId());
//            //Add a new edge that shouldn't exist
//            layoutNode.getChildren().add(a);
//            //this should fail
//            assertFalse(areNodesEqual(n, layoutNode));
//        }
//    }

}