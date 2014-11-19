package com.imperial.heap3d.snapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeapNode extends Node {

    private long id;
    private List<HeapNode> references;
    private List<ArrayNode> arrays;
    private List<StringNode> strings;
    private Map<String, Object> primitives;

    public HeapNode(String name, long id) {
        super(name);
        this.id = id;
        this.references = new ArrayList<>();
        this.arrays = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.primitives = new HashMap<>();
    }

    public long getID(){
        return id;
    }

    public void addPrimitive(String name, Object value){
        primitives.put(name, value);
    }

    public void addArray(ArrayNode arrayNode){
        arrays.add(arrayNode);
    }

    public void addString(StringNode stringNode) {
        strings.add(stringNode);
    }

    public void addReference(HeapNode heapNode){
        references.add(heapNode);
    }
}