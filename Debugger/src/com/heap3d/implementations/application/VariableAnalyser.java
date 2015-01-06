package com.heap3d.implementations.application;

import com.heap3d.implementations.layout.Bridge;
import com.heap3d.implementations.snapshot.StackNode;
import com.heap3d.interfaces.application.IVariableAnalyser;
import com.heap3d.interfaces.factories.INodeBuilder;
import com.heap3d.utilities.Check;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.event.LocatableEvent;

import java.util.Collection;

/**
 * Created by om612 on 01/12/14.
 */
public class VariableAnalyser implements IVariableAnalyser {

    private final INodeBuilder _nodeBuilder;
    private final Bridge _bridge;

    public VariableAnalyser(INodeBuilder nodeBuilder, Bridge bridge) {
        _nodeBuilder = Check.notNull(nodeBuilder, "nodeBuilder");
        _bridge = Check.notNull(bridge, "bridge");
    }

    @Override
    public void analyseVariables(LocatableEvent event) {
        event = Check.notNull(event, "event");
        try {
            StackFrame stackFrame = event.thread().frame(0);
            Collection<StackNode> stackNodes = _nodeBuilder.build(stackFrame);
            _bridge.giveNodes(stackNodes);
        }
        catch (IncompatibleThreadStateException e) { e.printStackTrace(); }
    }
}
