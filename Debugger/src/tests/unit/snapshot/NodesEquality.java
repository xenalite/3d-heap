package tests.unit.snapshot;

import com.imperial.heap3d.implementations.snapshot.ObjectNode;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by oskar on 27/11/14.
 */
public class NodesEquality {

    @Test
    public void sameId_Equals() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(1);

        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
    }

    @Test
    public void differentId_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        ObjectNode n2 = new ObjectNode(2);

        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }

    @Test
    public void sameId_differentNodeType_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode(1);
        StackNode n2 = new StackNode("lv", n1);

        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }
}
