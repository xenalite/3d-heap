package tests.unit.snapshot;

import com.imperial.heap3d.snapshot.ObjectNode;
import com.imperial.heap3d.snapshot.StackNode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by oskar on 27/11/14.
 */
public class NodesEquality {

    @Test
    public void sameId_sameName_Equals() {
        ObjectNode n1 = new ObjectNode("o1", 1);
        ObjectNode n2 = new ObjectNode("o1", 1);

        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
    }

    @Test
    public void sameId_differentName_Equals() {
        ObjectNode n1 = new ObjectNode("this", 2);
        ObjectNode n2 = new ObjectNode("o1", 2);

        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
    }

    @Test
    public void differentId_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("this", 1);
        ObjectNode n2 = new ObjectNode("this", 2);

        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }

    @Test
    public void sameId_differentNodeType_DoesNotEqual() {
        ObjectNode n1 = new ObjectNode("this", 1);
        StackNode n2 = new StackNode("root", 1);

        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
    }
}
