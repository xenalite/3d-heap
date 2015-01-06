package com.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.heap3d.utilities.GeometryUtils;
import com.heap3d.utilities.Pair;
import com.sun.jdi.Value;

import java.util.*;
import java.util.Map.Entry;

public class ArrayNode extends Node {
    private Map<Object, Object> _primitives;

    private List<Pair<Node,String>> _references;

    public ArrayNode(long id) {
        super(id);
        this._references = new ArrayList<>();
        this._primitives = new HashMap<>();
    }

    public void addElement(Node element, String index) { _references.add(Pair.create(element, index)); }

    @Override
    public List<Object> getPrimitives() {
        List<Object> primitives = new LinkedList<>();
        primitives.add(_references.size());
        return primitives;
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        return _references;
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForArrayNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ArrayNode && getId() == ((Node) o).getId();
    }

    public void addReference(Node first, String second) {
        _references.add(Pair.create(first, second));
    }

    public void addPrimitive(int index, Value value) {
    	_primitives.put(index, value);
    }

	@Override
	public Set<Entry<Object, Object>> getPrimitiveSet() {
		return _primitives.entrySet();
	}
}
