package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.imperial.heap3d.implementations.events.ProcessEventType.STARTED;

/**
 * Created by oskar on 05/12/14.
 */
public class Bridge {

    private final IRenderEngine _renderEngine;
    private final EventBus _eventBus;
    private HeapGraph _heapGraph;

    public Bridge(IRenderEngine adapter, EventBus eventBus) {
        _renderEngine = Check.NotNull(adapter, "adapter");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        // TODO - Dependency Injection
        _heapGraph = new HeapGraph(_renderEngine, _eventBus);
        List<Runnable> commands = new ArrayList<>();
        commands.add(_heapGraph::inLoop);
        _renderEngine.during(commands);
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent e) {
        if(e.type == STARTED) {
            _renderEngine.clear3DSpace();
        }
    }

    public void giveNodes(Collection<StackNode> stackNodes) {
        _heapGraph.giveNodes(stackNodes);
    }
}
