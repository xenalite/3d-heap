package com.imperial.heap3d.snapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeapNode extends IDNode {

    private List<HeapNode> references;
    private List<ArrayNode> arrays;
    private List<StringNode> strings;
    private Map<String, Object> primitives;

    public HeapNode(String name, long id) {
        super(name, id);
        this.references = new ArrayList<>();
        this.arrays = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.primitives = new HashMap<>();
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
    
    public Set<IDNode> walkHeap(){
    	
    	Set<IDNode> nodes = new HashSet<IDNode>();
    	
    	nodes.add(this);
    	
    	for(HeapNode hNode : references){
    		nodes.add(hNode);
    		for(IDNode childHNode : hNode.walkHeap())
    			nodes.add(childHNode);
    	}
    	
    	for(ArrayNode aNode : arrays)
    		nodes.add(aNode);
    	
    	for(StringNode sNode : strings)
    		nodes.add(sNode);
    	
		return nodes;
    }
    
    @Override
    public Set<IDNode> getChildren(){
    	
    	Set<IDNode> nodes = new HashSet<IDNode>();
    	
    	for(HeapNode hNode : references)
    		nodes.add(hNode);
    	
    	for(ArrayNode aNode : arrays)
    		nodes.add(aNode);
    	
    	for(StringNode sNode : strings)
    		nodes.add(sNode);
    	
		return nodes;
    }
}