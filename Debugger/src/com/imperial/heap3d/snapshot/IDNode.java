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
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IDNode)) {
            return false;
        }

        IDNode idNode = (IDNode) o;

        if (id != idNode.getID()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
	
}
