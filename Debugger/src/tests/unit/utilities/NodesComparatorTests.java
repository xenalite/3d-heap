package tests.unit.utilities;

import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.ObjectNode;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.NodesComparator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by om612 on 27/11/14.
 */
public class NodesComparatorTests {

    private NodesComparator SystemUnderTest;

    @Before
    public void SetUp() {
        SystemUnderTest = new NodesComparator();
    }

    @Test
    public void Test_SameNodeEquality_Equals() {
        Node n1 = new ObjectNode(1);
        assertTrue(SystemUnderTest.compare(n1, n1));
    }

    @Test
    public void Test_SingleNodeEquality_Equals() {
        Node n1 = new ObjectNode(1);
        Node n2 = new ObjectNode(1);

        boolean result = SystemUnderTest.compare(n1, n2);

        assertTrue(result);
    }

    @Test
    public void Test_SingleNodeEquality_DoesNotEqual() {
        Node n1 = new ObjectNode(1);
        Node n2 = new ObjectNode(2);

        boolean result = SystemUnderTest.compare(n1, n2);

        assertFalse(result);
    }

    @Test
    public void Test_ImmediateSelfReferenceEquality_Equals() {
        ObjectNode n1 = new ObjectNode(2);
        n1.addReference(n1, "self");

        boolean result = SystemUnderTest.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void Test_CyclicReferenceEquality_Equals() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        n1.addReference(n2, "root");
        n2.addReference(n3, "o1");
        n3.addReference(n1, "o2");

        boolean result = SystemUnderTest.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void Test_CyclicReferenceEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(4);
        ObjectNode n3 = new ObjectNode(3);
        n1.addReference(n2, "root");
        n2.addReference(n3, "o1");
        n3.addReference(n1, "o2");

        boolean result = SystemUnderTest.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void Test_CyclicReferenceAtTheEndEquality_Equals() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        n1.addReference(n2, "root");
        n2.addReference(n3, "o1");
        n3.addReference(n3, "o2");

        boolean result = SystemUnderTest.compare(n1, n1);

        assertTrue(result);
    }

    @Test
    public void Test_CyclicReferenceEquality_Complex_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(1);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        ObjectNode n5 = new ObjectNode(5);
        ObjectNode n6 = new ObjectNode(6);
        n1.addReference(n2, "this");
        n1.addReference(n3, "field1");
        n1.addReference(n4, "field2");
        n4.addReference(n5, "field3");
        n4.addReference(n6, "field4");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(1);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        ObjectNode n5b = new ObjectNode(5);
        ObjectNode n6b = new ObjectNode(1);
        n1b.addReference(n2b, "this");
        n1b.addReference(n3b, "field1");
        n1b.addReference(n4b, "field2");
        n4b.addReference(n5b, "field3");
        n4b.addReference(n6b, "field4");

        boolean result = SystemUnderTest.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void Test_CyclicReferenceEquality_Complex2_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        ObjectNode n5 = new ObjectNode(5);
        ObjectNode n6 = new ObjectNode(6);
        n1.addReference(n2, "root");
        n2.addReference(n3, "o1");
        n2.addReference(n5, "o2");
        n3.addReference(n4, "o3");
        n3.addReference(n5, "o4");
        n4.addReference(n2, "o5");
        n4.addReference(n1, "o6");
        n5.addReference(n6, "o7");
        n6.addReference(n1, "o8");

        ObjectNode nb1 = new ObjectNode(1);
        ObjectNode nb2 = new ObjectNode(2);
        ObjectNode nb3 = new ObjectNode(3);
        ObjectNode nb4 = new ObjectNode(4);
        ObjectNode nb5 = new ObjectNode(5);
        ObjectNode nb6 = new ObjectNode(66);
        nb1.addReference(nb2, "root");
        nb2.addReference(nb3, "o1");
        nb2.addReference(nb5, "o2");
        nb3.addReference(nb4, "o3");
        nb3.addReference(nb5, "o4");
        nb4.addReference(nb2, "o5");
        nb4.addReference(nb1, "o6");
        nb5.addReference(nb6, "o7");
        nb6.addReference(nb1, "o8");

        boolean result = SystemUnderTest.compare(n1, nb1);

        assertFalse(result);
    }

    @Test
    public void Test_CyclicReferenceEquality_Complex3_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        ObjectNode n5 = new ObjectNode(5);
        n1.addReference(n2, "f1");
        n2.addReference(n3, "f2");
        n2.addReference(n4, "f3");
        n3.addReference(n4, "f4");
        n4.addReference(n2, "f5");
        n4.addReference(n5, "f6");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(2);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        ObjectNode n5b = new ObjectNode(55);
        n1b.addReference(n2b, "f1");
        n2b.addReference(n3b, "f2");
        n2b.addReference(n4b, "f3");
        n3b.addReference(n4b, "f4");
        n4b.addReference(n2b, "f5");
        n4b.addReference(n5b, "f6");

        boolean result = SystemUnderTest.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void Test_LinkedListEquality_Equals() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        n1.addReference(n2, "f1");
        n2.addReference(n3, "f2");
        n3.addReference(n4, "f3");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(2);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        n1b.addReference(n2b, "f1");
        n2b.addReference(n3b, "f2");
        n3b.addReference(n4b, "f3");

        boolean result = SystemUnderTest.compare(n1, n1b);

        assertTrue(result);
    }

    @Test
    public void Test_LinkedListEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(8);
        n1.addReference(n2, "f1");
        n2.addReference(n3, "f2");
        n3.addReference(n4, "f3");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(2);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        n1b.addReference(n2b, "f1");
        n2b.addReference(n3b, "f2");
        n3b.addReference(n4b, "f3");

        boolean result = SystemUnderTest.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void Test_BinaryTreeEquality_Equals() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        ObjectNode n5 = new ObjectNode(5);
        n1.addReference(n2, "f2").addReference(n3, "f4");
        n2.addReference(n4, "f3");
        n3.addReference(n5, "f5");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(2);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        ObjectNode n5b = new ObjectNode(5);
        n1b.addReference(n2b,"f2").addReference(n3b,"f3");
        n2b.addReference(n4b, "f4");
        n3b.addReference(n5b, "f5");

        boolean result = SystemUnderTest.compare(n1, n1b);
        assertTrue(result);
    }

    @Test
    public void Test_BinaryTreeEquality_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);
        ObjectNode n3 = new ObjectNode(3);
        ObjectNode n4 = new ObjectNode(4);
        ObjectNode n5 = new ObjectNode(5);
        n1.addReference(n2,"f2").addReference(n3,"f3");
        n2.addReference(n4,"f4");
        n3.addReference(n5,"f5");

        ObjectNode n1b = new ObjectNode(1);
        ObjectNode n2b = new ObjectNode(2);
        ObjectNode n3b = new ObjectNode(3);
        ObjectNode n4b = new ObjectNode(4);
        ObjectNode n5b = new ObjectNode(9);
        n1b.addReference(n2b,"f2").addReference(n3b,"f3");
        n2b.addReference(n4b,"f4");
        n3b.addReference(n5b,"f5");

        boolean result = SystemUnderTest.compare(n1, n1b);

        assertFalse(result);
    }

    @Test
    public void Test_StackNode_ChangeBack_DoesNotEqual(){
        ObjectNode a1 = new ObjectNode(1);
        ObjectNode l1 = new ObjectNode(2);
        l1.addReference(a1,"a1");
        StackNode s1 = new StackNode("l1",l1);

        ObjectNode l2 = new ObjectNode(2);
        ObjectNode n2 = new ObjectNode(3);
        StackNode s2 = new StackNode("l2",l2);
        l2.addReference(n2,"n2");

        boolean result = SystemUnderTest.compare(s1, s2);

        assertFalse(result);
    }
}