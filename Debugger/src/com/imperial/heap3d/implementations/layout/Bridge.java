package com.imperial.heap3d.implementations.layout;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.events.NodeEvent;
import com.imperial.heap3d.implementations.events.NodeSelectionEvent;
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

            for (Entry<Node,Shape> entry : _heapGraph.getCurrentNodes()) {
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
        
        if(selectedAnimation != null){
        	
        	selectedAnimation.step();
        	
        	boolean doubleClick = _renderEngine.isDoubleClick();
        	
        	if(io != null){
        		io.enter();
        	}
        	
        	if(doubleClick){
        		if(io == null){
        			if(selected == null) return;
        			float[] pos = selected.getPosition();
        			_renderEngine.setCameraPositionSmooth(pos[0], pos[1], pos[2]);
        			io = new InsideObject(_renderEngine, selected, _renderEngine.getCameraPos(), selectedNode.getPrimitiveSet());
        		}else{
        			_renderEngine.setBackgroundColour(0,0,0, 1);
        			_renderEngine.clear3DSpace();
        			_renderEngine.switchActiveLayer(0);
        			io.exit();
        			io = null;
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
        stackNodes = Check.notNull(stackNodes, "stackNodes");
        _heapGraph.giveNodes(stackNodes);
        _eventBus.post(new NodeEvent(stackNodes));
    }
}