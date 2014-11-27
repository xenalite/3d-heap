package com.imperial.heap3d.snapshot;

import com.graphics.shapes.Colour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayNode extends Node {

    private List<Node> _elements;

    public ArrayNode(String name, long id) {
        super(name, id);
        this._elements = new ArrayList<>();
        this.colour = Colour.YELLOW;
    }

    public void addElement(Node element) { _elements.add(element); }

    @Override
    public List<Object> getPrimitives() {
        // TODO -- technically not the length of the array
        List<Object> primitives = new LinkedList<>();
        primitives.add(_elements.size());
        return primitives;
    }

    @Override
    public List<Node> getReferences() {
        return _elements;
    }
}
