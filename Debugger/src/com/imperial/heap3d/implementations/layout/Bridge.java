package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.utilities.Check;

import static com.imperial.heap3d.implementations.events.ProcessEventType.STARTED;

/**
 * Created by oskar on 05/12/14.
 */
public class Bridge {

    private final NullRunnable _nullRunnable = new NullRunnable();
    private final RenderEngineAdapter _adapter;
    private final EventBus _eventBus;
    private HeapGraph _heapGraph;

    public Bridge(RenderEngineAdapter adapter, EventBus eventBus) {
        _adapter = Check.NotNull(adapter, "adapter");
        _adapter.before(_nullRunnable);
        _adapter.during(_nullRunnable);
        _adapter.after(_nullRunnable);
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _eventBus.register(this);
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent event) {
        if(event.type == STARTED) {
            _heapGraph = new HeapGraph(_adapter, _eventBus);
            _adapter.during(_heapGraph::inLoop);
        }
    }
}
