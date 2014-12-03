package com.imperial.heap3d.implementations.application;

import com.imperial.heap3d.implementations.factories.HeapGraphFactory;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.application.IVariableAnalyser;
import com.imperial.heap3d.interfaces.factories.INodeBuilder;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;

import java.util.Collection;

/**
 * Created by om612 on 01/12/14.
 */
public class VariableAnalyser implements IVariableAnalyser {

    private final INodeBuilder _nodeBuilder;
    private final HeapGraphFactory _heapGraphFactory;

    public VariableAnalyser(INodeBuilder nodeBuilder, HeapGraphFactory heapGraphFactory) {
        _nodeBuilder = Check.NotNull(nodeBuilder, "nodeBuilder");
        _heapGraphFactory = Check.NotNull(heapGraphFactory, "heapGraphFactory");
    }

    @Override
    public void analyseVariables(LocatableEvent event) {
        event = Check.NotNull(event, "event");
        try {
            StackFrame stackFrame = event.thread().frame(0);
            Collection<StackNode> stackNodes = _nodeBuilder.build(stackFrame);
            _heapGraphFactory.create().giveStackNodes(stackNodes);
        }
        catch (IncompatibleThreadStateException e) { e.printStackTrace(); }
    }
}
