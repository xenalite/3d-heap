package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import com.imperial.heap3d.utilities.Pair;

import java.util.*;

/**
 * Created by costica on 12/31/2014.
 */
public class StaticNode extends StackNode {

    private Set<String> referencingVars;
    private List<Pair<Node,String>> _references;
    private Map<String, Object> _primitives;

    public StaticNode(String name, long id) {
        super(name, id);
        referencingVars = new HashSet<>();
        _references = new ArrayList<>();
        _primitives = new HashMap<>();
    }

    public void addReferencingVar(String instance) {
        referencingVars.add(instance);
    }

    public void removeReferencesInstance(String instance) {
        referencingVars.remove(instance);
    }

    public void addPrimitive(String name, Object value) {
        _primitives.put(name, value);
    }

    public StaticNode addReference(Node node, String name) {
        _references.add(Pair.create(node, name));
        return this;
    }

    @Override
    public boolean hasReference() {
        return getReferences().size() > 0;
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        return _references;
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForStaticStackNode();
    }
}
