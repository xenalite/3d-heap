package com.heap3d.layout;

import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import org.apache.commons.collections15.collection.UnmodifiableCollection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;

import static org.junit.Assert.*;

public class GraphImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void addParallelEdgeFails() throws Exception {
        GraphImpl<LayoutNode, Integer> graph = new GraphImpl<LayoutNode, Integer>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        LayoutNode b = new LayoutNode("low",0,0,0);
        LayoutNode c = new LayoutNode("high", 1000,1000,1000);
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);

        graph.addEdge(0,a,b);
        assertFalse(graph.addEdge(0, a, b));
        assertFalse(graph.addEdge(1,a,b));
    }

    @Test
    public void addEdgeAddsMissingNodes() throws Exception {
        GraphImpl<LayoutNode, Integer> graph = new GraphImpl<LayoutNode, Integer>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        LayoutNode b = new LayoutNode("low",0,0,0);


        graph.addEdge(0,a,b);

        Collection<LayoutNode> vertices = graph.getVertices();
        assertTrue(vertices.contains(a));
        assertTrue(vertices.contains(b));
    }

    @Test
    public void deleteVertexDeletesEdges() throws Exception {
        GraphImpl<LayoutNode, Integer> graph = new GraphImpl<LayoutNode, Integer>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        LayoutNode b = new LayoutNode("low",0,0,0);

        graph.addEdge(0,a,b);

        graph.removeVertex(a);

        assertTrue(graph.getEdgeCount() == 0);
    }

    @Test()
    public void deleteVertexFromCollectionFails()  {
        GraphImpl<LayoutNode, Integer> graph = new GraphImpl<LayoutNode, Integer>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        LayoutNode b = new LayoutNode("low",0,0,0);

        graph.addEdge(0,a,b);

        Collection<LayoutNode> vertices = graph.getVertices();
        exception.expect(UnsupportedOperationException.class);
        vertices.remove(a);

    }

}