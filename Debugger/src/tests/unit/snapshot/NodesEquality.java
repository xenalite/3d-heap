package tests.unit.snapshot;

import com.imperial.heap3d.snapshot.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by oskar on 27/11/14.
 */
public class NodesEquality {

    @Test
    public void singleObjectEquality() {
        ObjectNode n1 = new ObjectNode("o1", 10);
        ObjectNode n2 = new ObjectNode("o1", 10);
        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));

        n1 = new ObjectNode("o1", 10);
        n2 = new ObjectNode("o2", 11);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }

    @Test
    public void nestedObjectsEquality() {
        ObjectNode n1 = new ObjectNode("o1", 1);
        n1.addReference(new ObjectNode("o2", 2));
        n1.addReference(new ObjectNode("o3", 3));

        ObjectNode n2 = new ObjectNode("o3", 1);
        n2.addReference(new ObjectNode("o4", 2));
        n2.addReference(new ObjectNode("o5", 3));
        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));

        n1 = new ObjectNode("o1", 1);
        n1.addReference(new ObjectNode("o2", 3));
        n1.addReference(new ObjectNode("o3", 2));

        n2 = new ObjectNode("o3", 1);
        n2.addReference(new ObjectNode("o4", 2));
        n2.addReference(new ObjectNode("o5", 3));
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }

    @Test
    public void linkedListEquality() {
        ObjectNode n1 = new ObjectNode("o1", 1);
        ObjectNode n2 = new ObjectNode("o2", 2);
        ObjectNode n3 = new ObjectNode("o3", 3);
        ObjectNode n4 = new ObjectNode("o4", 4);
        ObjectNode n5 = new ObjectNode("o5", 5);
        n1.addReference(n2);
        n2.addReference(n3);
        n3.addReference(n4);
        n4.addReference(n5);

        ObjectNode n1b = new ObjectNode("o1", 1);
        ObjectNode n2b = new ObjectNode("o2", 2);
        ObjectNode n3b = new ObjectNode("o3", 3);
        ObjectNode n4b = new ObjectNode("o4", 4);
        ObjectNode n5b = new ObjectNode("o5", 5);
        n1b.addReference(n2b);
        n2b.addReference(n3b);
        n3b.addReference(n4b);
        n4b.addReference(n5b);

        assertTrue(n1.equals(n1b));
        assertTrue(n1b.equals(n1));
    }

    @Test
    public void binaryTreeEquality() {
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

        assertTrue(n1.equals(n1b));
        assertTrue(n1b.equals(n1));
    }

    @Test
    public void selfReferenceEquality() {
        ObjectNode n1 = new ObjectNode("root", 1);
        n1.addReference(n1);

        ObjectNode n2 = new ObjectNode("this", 1);
        n2.addReference(n2);

        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
    }
}
