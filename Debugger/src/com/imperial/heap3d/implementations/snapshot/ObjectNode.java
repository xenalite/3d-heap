package com.imperial.heap3d.implementations.snapshot;

import com.graphics.shapes.Shape;
import com.imperial.heap3d.utilities.GeometryUtils;
import com.imperial.heap3d.utilities.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ObjectNode extends Node {

    private List<Pair<Node,String>> _references;
    private Map<Object, Object> _primitives;

    public ObjectNode(long id) {
        super(id);
        _references = new ArrayList<>();
        _primitives = new HashMap<>();
    }

    public void addPrimitive(String name, Object value) {
        _primitives.put(name, value);
    }

    public ObjectNode addReference(Node node, String name) {
        _references.add(Pair.create(node, name));
        return this;
    }

    @Override
    public Object getPrimitives() {
    	String res = "";
    	for(Entry e : _primitives.entrySet()){
    		res += ("(" + e.getKey() + " : " + e.getValue() + ")");
    	}
        return res;
    }

    @Override
    public List<Pair<Node, String>> getReferences() {
        return _references;
    }

    @Override
    public Shape createShape() {
        return GeometryUtils.createCubeForObjectNode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ObjectNode && getId() == ((Node) o).getId();
    }

	@Override
	public Set<Entry<Object, Object>> getPrimitiveSet() {
		return _primitives.entrySet();
	}
}