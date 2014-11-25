package com.imperial.heap3d.snapshot;

public abstract class IdNode extends Node {

	private long _id;

	public IdNode(String name, long id) {
		super(name);
		this._id = id;
	}
	
	public long getId(){
		return _id;
	}

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof IdNode && _id == ((IdNode) o).getId();
    }

    @Override
    public int hashCode() {
        return (int) (_id ^ (_id >>> 32));
    }
}
