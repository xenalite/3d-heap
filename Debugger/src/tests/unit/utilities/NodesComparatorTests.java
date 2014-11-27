package tests.unit.utilities;

import com.imperial.heap3d.snapshot.Node;
import com.imperial.heap3d.snapshot.ObjectNode;
import com.imperial.heap3d.utilities.NodesComparator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by om612 on 27/11/14.
 */
public class NodesComparatorTests {

    private NodesComparator _sut;

    @Before
    public void setUp() {
        _sut = new NodesComparator();
    }

    @Test
    public void sameNodeEquality_Equals() {
        Node n1 = new ObjectNode("this", 1);
        assertTrue(_sut.compare(n1, n1));
    }

    @Test
    public void singleNodeEquality_Equals() {
        Node n1 = new ObjectNode("this", 1);
        Node n2 = new ObjectNode("this", 1);

        boolean result = _sut.compare(n1, n2);

        assertTrue(result);
    }

    @Test
    public void singleNodeEquality_DoesNotEqual() {
        Node n1 = new ObjectNode("this", 1);
        Node n2 = new ObjectNode("this", 2);

        boolean result = _sut.compare(n1, n2);

        assertFalse(result);
    }

    @Test
    public void immediateSelfReferenceEquality_Equals() {
        ObjectNode n1 = new ObjectNode("root", 2);
        n1.addReference(n1);

        boolean result = _sut.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void cyclicReferenceEquality_Equals() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("o1", 2);
        ObjectNode n3 = new ObjectNode("o2", 3);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n1);

        boolean result = _sut.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void cyclicReferenceEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("o1", 4);
        ObjectNode n3 = new ObjectNode("o2", 3);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n1);

        boolean result = _sut.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void cyclicReferenceEquality_Complex_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("this", 1);
        ObjectNode n2 = new ObjectNode("field1", 1);
        ObjectNode n3 = new ObjectNode("field2", 3);
        ObjectNode n4 = new ObjectNode("field3", 4);
        ObjectNode n5 = new ObjectNode("field3_left", 5);
        ObjectNode n6 = new ObjectNode("field3_right", 6);
        n1.addReference(n2);
        n1.addReference(n3);
        n1.addReference(n4);
        n4.addReference(n5);
        n4.addReference(n6);


        ObjectNode n1b = new ObjectNode("this", 1);
        ObjectNode n2b = new ObjectNode("field1", 1);
        ObjectNode n3b = new ObjectNode("field2", 3);
        ObjectNode n4b = new ObjectNode("field3", 4);
        ObjectNode n5b = new ObjectNode("field3_left", 5);
        ObjectNode n6b = new ObjectNode("field3_right", 1);
        n1b.addReference(n2b);
        n1b.addReference(n3b);
        n1b.addReference(n4b);
        n4b.addReference(n5b);
        n4b.addReference(n6b);

        boolean result = _sut.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void cyclicReferenceAtTheEndEquality_Equals() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("o1", 2);
        ObjectNode n3 = new ObjectNode("o2", 3);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n3);

        boolean result = _sut.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void linkedListEquality_Equals() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("node1", 2);
        ObjectNode n3 = new ObjectNode("node2", 3);
        ObjectNode n4 = new ObjectNode("node3", 4);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n4);

        ObjectNode n1b = new ObjectNode("root", 1);
        ObjectNode n2b = new ObjectNode("node1", 2);
        ObjectNode n3b = new ObjectNode("node2", 3);
        ObjectNode n4b = new ObjectNode("node3", 4);
        n1b.addReference(n2b);
        n2b.addReference(n3b);
        n3b.addReference(n4b);

        boolean result = _sut.compare(n1, n1b);

        assertTrue(result);
    }

    @Test
    public void linkedListEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("node1", 2);
        ObjectNode n3 = new ObjectNode("node2", 3);
        ObjectNode n4 = new ObjectNode("node3", 8);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n4);

        ObjectNode n1b = new ObjectNode("root", 1);
        ObjectNode n2b = new ObjectNode("node1", 2);
        ObjectNode n3b = new ObjectNode("node2", 3);
        ObjectNode n4b = new ObjectNode("node3", 4);
        n1b.addReference(n2b);
        n2b.addReference(n3b);
        n3b.addReference(n4b);

        boolean result = _sut.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void binaryTreeEquality_Equals() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("left", 2);
        ObjectNode n3 = new ObjectNode("right", 3);
        ObjectNode n4 = new ObjectNode("left_right", 4);
        ObjectNode n5 = new ObjectNode("right_left", 5);
        n1.addReference(n2).addReference(n3);
        n2.addReference(n4);
        n3.addReference(n5);

        ObjectNode n1b = new ObjectNode("root", 1);
        ObjectNode n2b = new ObjectNode("left", 2);
        ObjectNode n3b = new ObjectNode("right", 3);
        ObjectNode n4b = new ObjectNode("left_right", 4);
        ObjectNode n5b = new ObjectNode("right_left", 5);
        n1b.addReference(n2b).addReference(n3b);
        n2b.addReference(n4b);
        n3b.addReference(n5b);

        boolean result = _sut.compare(n1, n1b);

        assertTrue(result);
    }

    @Test
    public void binaryTreeEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("root", 1);
        ObjectNode n2 = new ObjectNode("left", 2);
        ObjectNode n3 = new ObjectNode("right", 3);
        ObjectNode n4 = new ObjectNode("left_right", 4);
        ObjectNode n5 = new ObjectNode("right_left", 5);
        n1.addReference(n2).addReference(n3);
        n2.addReference(n4);
        n3.addReference(n5);

        ObjectNode n1b = new ObjectNode("root", 1);
        ObjectNode n2b = new ObjectNode("left", 2);
        ObjectNode n3b = new ObjectNode("right", 3);
        ObjectNode n4b = new ObjectNode("left_right", 4);
        ObjectNode n5b = new ObjectNode("right_left", 9);
        n1b.addReference(n2b).addReference(n3b);
        n2b.addReference(n4b);
        n3b.addReference(n5b);

        boolean result = _sut.compare(n1, n1b);

        assertFalse(result);
    }
}
