package com.imperial.heap3d.snapshot;

import java.util.HashSet;
import java.util.Set;

public class StackNode extends Node {

    private Object value;

    public StackNode(String name, Object value) {
        super(name);
        this.value = value;
    }
    
    public Object getValue(){
    	return value;
    }
    
    public boolean doesRefNode(){
    	return value instanceof Node;
    }
    
    /**
     * Walk the heap from the stack node
     * @return The entire heap from the stack elem, or empty if it is a primitive
     */
    public Set<IDNode> walkHeap(){
    	
    	if(value instanceof HeapNode)
    		return ((HeapNode)value).walkHeap();
    	
    	Set<IDNode> nodes = new HashSet<IDNode>();
    	
    	if(value instanceof IDNode)
    		nodes.add((IDNode) value);
    	
    	return nodes;
    }
}
