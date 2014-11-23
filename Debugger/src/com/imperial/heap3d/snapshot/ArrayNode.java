package com.imperial.heap3d.snapshot;

import java.util.ArrayList;
import java.util.List;

import com.graphics.shapes.Colour;

public class ArrayNode extends IDNode {

    private List<Object> elements;

    public ArrayNode(String name, long id) {
        super(name, id);
        this.elements = new ArrayList<>();
        this.colour = Colour.YELLOW;
    }

    public Object getElementAt(int index) {
        return elements.get(index);
    }

    public List<Object> getElements() {
        return elements;
    }

    public void addPrimitive(Object value) {
        elements.add(value);
    }

    public void addArray(ArrayNode arrayNode) {
        elements.add(arrayNode);
    }

    public void addString(StringNode stringNode) {
        elements.add(stringNode);
    }

    public void addReference(HeapNode heapNode) {
        elements.add(heapNode);
    }
}
