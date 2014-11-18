package com.imperial.heap3d.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by slj12 on 18/11/14.
 */
public class HeapNode extends Node{

    private long id;
    private List<HeapNode> heapReferences = new ArrayList<HeapNode>();
    private Map<String, Object> childPrimitives = new HashMap<String, Object>();

    public HeapNode(String name, long id){
        super(name);
        this.id = id;
    }

    public void addHeapNodeRef(HeapNode heapNode){
        heapReferences.add(heapNode);
    }

    public long getID(){
        return id;
    }

    public void setChildPrimitive(String name, Object value){
        childPrimitives.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(Entry<String, Object> e : childPrimitives.entrySet())
            builder.append("Prim: Name: ").append(e.getKey()).append(", Value: ").append(e.getValue()).append(System.lineSeparator());

        for(HeapNode o : heapReferences)
           builder.append(o.toString()).append(System.lineSeparator());

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof HeapNode)) {
            return false;
        }

        HeapNode heapNode = (HeapNode) o;

        if (id != heapNode.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}