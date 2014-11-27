package com.imperial.heap3d.utilities;

import com.imperial.heap3d.snapshot.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by oskar on 27/11/14.
 */
public class NodesComparator {

    public boolean compare(Node n1, Node n2) {
        if(!n1.equals(n2))
            return false;

        Set<Long> seenIds = new HashSet<>();
        seenIds.add(n1.getId());
        List<Node> children1 = n1.getReferences();
        List<Node> children2 = n2.getReferences();
        Iterator<Node> it1 = children1.iterator();
        Iterator<Node> it2 = children2.iterator();
        if(children1.size() != children2.size())
            return false;
        while(it1.hasNext()) {
            Node c1 = it1.next();
            Node c2 = it2.next();
        }

        return false;
    }
}
