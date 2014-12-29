package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.events.NodeEvent;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.implementations.layout.animation.SelectedAnimation;
import com.imperial.heap3d.implementations.snapshot.Node;
import com.imperial.heap3d.implementations.snapshot.StackNode;
import com.imperial.heap3d.utilities.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import static com.imperial.heap3d.implementations.events.ProcessEventType.STARTED;

/**
 * Created by oskar on 05/12/14.
 */
public class Bridge {

    public static IRenderEngine _renderEngine = null;
    private final EventBus _eventBus;
    private HeapGraph _heapGraph;

    public Bridge(IRenderEngine adapter, EventBus eventBus) {
        _renderEngine = Check.NotNull(adapter, "adapter");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _eventBus.register(this);
        // TODO - Dependency Injection
        _heapGraph = new HeapGraph(_renderEngine);
        List<Runnable> commands = new ArrayList<>();
        commands.add(_heapGraph::inLoop);
        commands.add(this::selectionMethod);
        _renderEngine.during(commands);
    }

    private Shape lastSelectedShape = null;
    private SelectedAnimation selectedAnimation;

    private void selectionMethod() {
        Shape selected = _renderEngine.getSelectedShape();
        
        if (selected != null && selected != lastSelectedShape) {
            lastSelectedShape = selected;

            for (Entry<Node,Shape> entry : _heapGraph.getCurrentNodes()) {
                Node node = entry.getKey();
                Shape s = entry.getValue();
                if (s == selected) {
                    if (selectedAnimation != null) {
                        selectedAnimation.finish();
                        selectedAnimation = null;
                    }

                    selectedAnimation = new SelectedAnimation(s);

                    String message = String.format("Selected Node: %s \nchildren: %s \nprimitives: %s",
                            node.getName(), node.getReferences(), node.getPrimitives());
                    _eventBus.post(new ProcessEvent(ProcessEventType.SELECT, message));
                    break;
                }
            }
        }
        
        if(selectedAnimation != null)
        	selectedAnimation.step();
        
        
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent e) {
        if(e.type == STARTED) {
            _renderEngine.clear3DSpace();
        }
    }

    public void giveNodes(Collection<StackNode> stackNodes) {
        stackNodes = Check.NotNull(stackNodes, "stackNodes");
        _heapGraph.giveNodes(stackNodes);
        _eventBus.post(new NodeEvent(stackNodes));
    }
}
