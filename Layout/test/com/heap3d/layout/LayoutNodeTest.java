package com.heap3d.layout;

import org.junit.Test;

import static org.junit.Assert.*;

public class LayoutNodeTest {

    @Test
    public void testHashCode() throws Exception {

    }

    @Test
    public void testEqualsFails() throws Exception {
        LayoutNode a = new LayoutNode("a",0,0,0);
        LayoutNode b = new LayoutNode("b",0,0,0);
        assertFalse(a.equals(b));
    }

    @Test
    public void testEqualsReflexiveFails() throws Exception {
        LayoutNode a = new LayoutNode("a",0,0,0);
        LayoutNode b = new LayoutNode("b",0,0,0);
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void testEqualsReflexivePass() throws Exception {
        LayoutNode a = new LayoutNode("a",0,0,0);
        LayoutNode b = new LayoutNode("a",0,0,0);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }

    @Test
    public void testEqualsHascodeConsitencyPass() throws Exception {
        LayoutNode a = new LayoutNode("a",0,0,0);
        LayoutNode b = new LayoutNode("a",0,0,0);
        assertTrue(a.equals(b));
        assertTrue(a.hashCode() == b.hashCode());
    }

    @Test
    public void testEqualsHascodeConsitencyFail() throws Exception {
        LayoutNode a = new LayoutNode("a",0,0,0);
        LayoutNode b = new LayoutNode("b",0,0,0);
        assertFalse(a.equals(b));
        assertFalse(a.hashCode() == b.hashCode());
    }
}