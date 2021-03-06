package com.heap3d.implementations.node;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.heap3d.utilities.GeometryUtils;
import com.heap3d.utilities.Pair;

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
        colour = Colour.AQUA;
        treeName = name;
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
    public boolean equals(Object o) {
        return this == o || o instanceof StaticNode &&
                getId() == ((StaticNode) o).getId() &&
                Objects.equals(getName(), ((StaticNode) o).getName());
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
