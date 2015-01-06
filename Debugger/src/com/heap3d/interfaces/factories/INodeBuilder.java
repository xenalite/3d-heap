package com.heap3d.interfaces.factories;

import com.heap3d.implementations.snapshot.StackNode;
import com.sun.jdi.StackFrame;

import java.util.Collection;

/**
 * Created by om612 on 01/12/14.
 */
public interface INodeBuilder {

    public Collection<StackNode> build(StackFrame stackFrame);
}
