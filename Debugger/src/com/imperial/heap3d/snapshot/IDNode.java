package com.imperial.heap3d.snapshot;

import java.util.HashSet;
import java.util.Set;

public abstract class IDNode extends Node{

	private long id;
	
	public IDNode(String name, long id) {
		super(name);
		this.id = id;
	}
	
	public long getID(){
		return id;
	}
	
	public Set<IDNode> getChildren(){
		return new HashSet<IDNode>();
	}
	
}
