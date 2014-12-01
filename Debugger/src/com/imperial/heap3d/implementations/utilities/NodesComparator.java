package com.imperial.heap3d.implementations.utilities;

import com.imperial.heap3d.implementations.snapshot.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by oskar on 27/11/14.
 */
public class NodesComparator {

    public boolean compare(Node n1, Node n2) {
        return compare(n1, n2, new HashSet<>());
    }

    private boolean compare(Node n1, Node n2, Set<Long> seenIds) {
        if (!n1.equals(n2))
            return false;
        if (seenIds.contains(n1.getId()))
            return true;

        List<Node> ln1 = n1.getReferences();
        List<Node> ln2 = n2.getReferences();
        if (ln1.size() != ln2.size())
            return false;

        Iterator<Node> it1 = ln1.iterator();
        Iterator<Node> it2 = ln2.iterator();
        seenIds.add(n1.getId());

        while (it1.hasNext()) {
            Node c1 = it1.next();
            Node c2 = it2.next();
            if(!compare(c1, c2, seenIds))
                return false;
        }
        return true;
    }
}
