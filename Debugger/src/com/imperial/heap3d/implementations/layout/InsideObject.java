package com.imperial.heap3d.implementations.layout;

import java.util.Map.Entry;
import java.util.Set;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;
import com.imperial.heap3d.implementations.snapshot.ArrayNode;
import com.imperial.heap3d.implementations.snapshot.Node;

public class InsideObject {

	private IRenderEngine _renderEngine;
	private float[] _currentPos;
	private boolean entered;
	private float inc;
	private Node _node;
	private float[] pos;
	private Colour c;
	
	public InsideObject(IRenderEngine renderEngine, Shape shape, float[] currentPos, Node node){
		_renderEngine = renderEngine;
		_currentPos = currentPos;
		_node = node;
		
		pos = shape.getPosition();
		c = shape.getColour();
		_renderEngine.setCameraPositionSmooth(pos[0], pos[1], pos[2]);
	}
	
	public void enter(){
		
		if (!entered && Math.round(_renderEngine.getCameraPos()[0]) == Math.round(pos[0])
				&& Math.round(_renderEngine.getCameraPos()[1]) == Math.round(pos[1])
				&& Math.round(_renderEngine.getCameraPos()[2]) == Math.round(pos[2])) {
			//We have reached target
			entered = true;
			_renderEngine.setBackgroundColour(c.getR(), c.getG(), c.getB(), 1);
			_renderEngine.switchActiveLayer(1);
			
			Set<Entry<Object, Object>> primitives = _node.getPrimitiveSet();
			
			if(primitives == null)
				return;
			
			for(Entry<Object, Object> e : primitives){
				String value = null;
				if(_node instanceof ArrayNode)
					value = "[" + e.getKey() + "] : " + e.getValue();
				else
					value = e.getKey() + " : " + e.getValue();
				_renderEngine.printTo3DSpace(pos[0], pos[1]+inc--, pos[2]-10, 0, 0, 0, 0.1f, value);
			}
		}
		
	}
	
	public void exit(){
		_renderEngine.setBackgroundColour(0.1f, 0.1f, 0.1f, 1);
		_renderEngine.clear3DSpace();
		_renderEngine.switchActiveLayer(0);
		_renderEngine.setCameraPosition(pos[0], pos[1], pos[2]);
		_renderEngine.setCameraPositionSmooth(_currentPos[0], _currentPos[1], _currentPos[2]);
	}

	public boolean inObject() {
		return entered;
	}
	
}
