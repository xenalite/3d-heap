package com.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.graphics.shapes.Shape;
import com.heap3d.implementations.events.NodeEvent;
import com.heap3d.implementations.events.NodeSelectionEvent;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.ProcessEventType;
import com.heap3d.implementations.animation.SelectedAnimation;
import com.heap3d.interfaces.render.IRenderEngine;
import com.heap3d.implementations.render.InsideObject;
import com.heap3d.implementations.node.Node;
import com.heap3d.implementations.node.StackNode;
import com.heap3d.utilities.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import static com.heap3d.implementations.events.ProcessEventType.STARTED;

/**
 * Created by oskar on 05/12/14.
 */
public class Bridge {

    public static IRenderEngine _renderEngine;
    private final EventBus _eventBus;
    private HeapGraph _heapGraph;

    public Bridge(IRenderEngine adapter, EventBus eventBus) {
        _renderEngine = Check.notNull(adapter, "adapter");
        _eventBus = Check.notNull(eventBus, "eventBus");
        _eventBus.register(this);
        _heapGraph = new HeapGraph(_renderEngine);
        List<Runnable> commands = new ArrayList<>();
        commands.add(_heapGraph::inLoop);
        commands.add(this::selectionMethod);
        _renderEngine.during(commands);
    }

    private Shape lastSelectedShape = null;
    private SelectedAnimation selectedAnimation;
    private InsideObject io;
    private Node selectedNode;

    private void selectionMethod() {
        Shape selected = _renderEngine.getSelectedShape();

        if (selected != null && selected != lastSelectedShape) {
            lastSelectedShape = selected;

            for (Entry<Node, Shape> entry : _heapGraph.getCurrentNodes()) {
                Node node = entry.getKey();
                Shape s = entry.getValue();
                if (s == selected) {
                    if (selectedAnimation != null) {
                        selectedAnimation.finish();
                        selectedAnimation = null;
                        selectedNode = null;
                    }

                    selectedAnimation = new SelectedAnimation(s);
                    selectedNode = node;

                    // TODO -- node name
                    String message = String.format("Selected Node: %s \nchildren: %s \nprimitives: %s",
                            node.toString(), node.getReferences(), node.getPrimitives());
                    _eventBus.post(new ProcessEvent(ProcessEventType.SELECT, message));
                    _eventBus.post(new NodeSelectionEvent(node));
                }
            }
        }

        if (selectedAnimation != null) {

            selectedAnimation.step();

            if (io != null) {
                io.enter();
            }

            if (_renderEngine.isDoubleClick()) {
                if (io == null) {
                    if (selected == null) return;
                    io = new InsideObject(_renderEngine, selected, _renderEngine.getCameraPos(), selectedNode);
                } else if (io.inObject()) {
                    io.exit();
                    io = null;
                }
            }
        }
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent e) {
        if (e.type == STARTED) {
            _renderEngine.clear3DSpace();
        }
    }

    public void giveNodes(Collection<StackNode> stackNodes) {
        stackNodes = Check.notNull(stackNodes, "stackNodes");
        _heapGraph.giveNodes(stackNodes);
        _eventBus.post(new NodeEvent(stackNodes));
    }
}