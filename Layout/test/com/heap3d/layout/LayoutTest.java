package com.heap3d.layout;

import edu.uci.ics.jung.graph.util.EdgeType;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LayoutTest {

    @Test
    public void testLayoutEmptyPass() {
        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.layout();
        //Make sure the layout doesn't throw an exceptions
    }

    @Test
    public void testLayoutPairNonDestructivePass() throws Exception {

        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();
        LayoutNode a = new LayoutNode("a", 0, 0, 0);
        LayoutNode b = new LayoutNode("b", 0, 0, 0);
        a.getChildren().add(b);
        b.getChildren().add(a);
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addEdge("ab", a, b, EdgeType.DIRECTED);
        graph.addEdge("ba",b,a, EdgeType.DIRECTED);

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.layout();

        //What is this testing
        assertTrue(a.getChildren().contains(b));
        assertTrue(b.getChildren().contains(a));
    }

    @Test
    public void testLayoutRootNodeFixedPosition() throws Exception {
        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        graph.addVertex(a);

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.layout(a);

        Point2D transform = layout.transform(a);
        assertEquals(0.0f, transform.getX(), 0.0f);
        assertEquals(0.0f, transform.getY(), 0.0f);
    }

    @Test
    public void testLayoutRootNodeFixedPositionAfterResize() throws Exception {
        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        graph.addVertex(a);

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.layout(a);
        layout.setSize(new Dimension(500,500));
        //layout.layout();

        Point2D transform = layout.transform(a);
        assertEquals(0.0f, transform.getX(), 0.0f);
        assertEquals(0.0f, transform.getY(), 0.0f);
    }

    @Test
    public void testLayoutRootNodeFixedPositionAfterResizeReLayout() throws Exception {
        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        graph.addVertex(a);

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.layout(a);
        layout.setSize(new Dimension(500,500));
        layout.layout();

        Point2D transform = layout.transform(a);
        assertEquals(0.0f, transform.getX(), 0.0f);
        assertEquals(0.0f, transform.getY(), 0.0f);
    }

    @Test
    public void testLayoutReSizingMultipleNodes() throws Exception {

        GraphImpl<LayoutNode, String> graph = new GraphImpl<LayoutNode, String>();
        LayoutNode a = new LayoutNode("root", 10, 10, 10);
        LayoutNode b = new LayoutNode("low",0,0,0);
        LayoutNode c = new LayoutNode("high", 1000,1000,1000);
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);

        Layout<LayoutNode, String> layout = new FRLayout<LayoutNode, String>(graph);
        layout.setLocation(a, new Point2D.Double(10,10));
        layout.setLocation(b, new Point2D.Double(0,0));
        layout.setLocation(c, new Point2D.Double(1000, 1000));
        layout.lock(a, true);
        layout.lock(b,true);
        layout.lock(c,true);

        layout.layout();

        Point2D aTransform = layout.transform(a);
        assertEquals(10, aTransform.getX(), 0.0f);
        assertEquals(10, aTransform.getY(), 0.0f);

        Point2D bTransform = layout.transform(b);
        assertEquals(0, bTransform.getX(), 0.0f);
        assertEquals(0, bTransform.getY(), 0.0f);

        Point2D cTransform = layout.transform(c);
        assertEquals(1000, cTransform.getX(), 0.0f);
        assertEquals(1000, cTransform.getY(), 0.0f);



    }

}