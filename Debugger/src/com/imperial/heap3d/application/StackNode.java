package com.imperial.heap3d.application;

/**
 * Created by slj12 on 18/11/14.
 */
public class StackNode extends Node{

    private Object value;

    public StackNode(String name, Object value) {
        super(name);
        this.value = value;
    }
    
    public Object getValue(){
    	return value;
    }
    
    public void setValue(Object value){
    	this.value = value;
    }
}
