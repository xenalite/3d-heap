package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.implementations.layout.animation.SelectedAnimation;
import com.imperial.heap3d.implementations.snapshot.Node;
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
        _eventBus.register(this);
        // TODO - Dependency Injection
        _heapGraph = new HeapGraph(_renderEngine, _eventBus);
        List<Runnable> commands = new ArrayList<>();
        commands.add(_heapGraph::inLoop);
        commands.add(this::selectionMethod);
        _renderEngine.during(commands);
    }

    private Shape lastSelectedShape = null;
    private SelectedAnimation selectedAnimation;

    private void selectionMethod() {
        Shape selected = _renderEngine.getSelectedShape();

        if (selectedAnimation != null && selected != null) {
            selectedAnimation.step();
        } else if (selectedAnimation != null) {
            selectedAnimation.finish();
            selectedAnimation = null;
        }

        if (selected != null && selected != lastSelectedShape) {
            lastSelectedShape = selected;

            for (Node node : _heapGraph.getCurrentNodes()) {
                if (node.getGeometry() == selected) {
                    if (selectedAnimation != null) {
                        selectedAnimation.finish();
                        selectedAnimation = null;
                    }

                    selectedAnimation = new SelectedAnimation(node);

                    String message = String.format("Selected Node: %s \nchildren: %s \nprimitives: %s", node.getName(), node.getReferences(), node.getPrimitives());
                    _eventBus.post(new ProcessEvent(ProcessEventType.SELECT, message));
                    break;
                }
            }
        }
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
