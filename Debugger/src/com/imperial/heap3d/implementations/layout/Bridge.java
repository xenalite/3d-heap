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

    private final RenderEngineAdapter _adapter;
    private final EventBus _eventBus;
    private HeapGraph _heapGraph;

    public Bridge(RenderEngineAdapter adapter, EventBus eventBus) {
        _adapter = Check.NotNull(adapter, "adapter");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _eventBus.register(this);
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent event) {
        if(event.type == STARTED) {
            _heapGraph = new HeapGraph(_adapter, _eventBus);
            List<Runnable> commands = new ArrayList<>();
            commands.add(_heapGraph::initialise);
            commands.add(_heapGraph::inLoop);

            _adapter.during(commands);
        }
    }

    public void giveStackNodes(Collection<StackNode> stackNodes) {
        _heapGraph.giveStackNodes(stackNodes);
    }
}
