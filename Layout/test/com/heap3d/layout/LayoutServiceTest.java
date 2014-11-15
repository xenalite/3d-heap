package com.heap3d.layout;

import com.heap3d.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LayoutServiceTest {

    @Parameterized.Parameters
    public static Collection<Object[]> createInstances()
    {
        return Arrays.asList(
                new Object[]{new FRLayout()},
                new Object[]{new ISOLayout()},
                new Object[]{new IterativeSpringBasedLayout()},
                new Object[]{new CircularLayout()}
        );
    }

    public LayoutServiceTest(LayoutService layout)  {
        this.layoutService = layout;
    }

    private static Boolean areNodesEqual(LayoutNode originalNode, LayoutNode newNode) {
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

    LayoutService layoutService;
    @Test
    public void testLayoutEmptyPass() throws Exception {
        Map<String, LayoutNode> graph = new HashMap<String, LayoutNode>();
        layoutService.layout(graph);

        assertEquals(0, graph.size());
    }

    @Test
    public void testLayoutPairNonDestructivePass() throws Exception {

        Map<String, LayoutNode> graph = new HashMap<String, LayoutNode>();
        LayoutNode a = new LayoutNode("a", 0, 0, 0);
        LayoutNode b = new LayoutNode("b", 0, 0, 0);
        a.getChildren().add(b);
        b.getChildren().add(a);
        graph.put(a.getId(), a);
        graph.put(b.getId(), b);
        layoutService.layout(graph);

        assertTrue(a.getChildren().contains(b));
        assertTrue(b.getChildren().contains(a));
    }

    @Test
    public void testLayoutRootNodeFixedPosition() throws Exception {
        Map<String, LayoutNode> graph = new HashMap<String, LayoutNode>();
        LayoutNode n = new LayoutNode("id", 10, 10, 10);
        graph.put(n.getId(), n);
        layoutService.layout(graph, n.getId());

        assertEquals(0.0f, n.x(), 0.0f);
        assertEquals(0.0f, n.y(), 0.0f);
    }

    @Test
    public void testLayoutReSizing() throws Exception {

        Map<String, LayoutNode> graph = Graph.randomGraph(500, 0.1, 1);
        layoutService.layout(graph);

        for(LayoutNode n : graph.values())
        {
            float x = n.x();
            float y = n.y();
            assertTrue(x < 1000 && x > 0);
            assertTrue(y < 1000 && y > 0);
        }

    }


}