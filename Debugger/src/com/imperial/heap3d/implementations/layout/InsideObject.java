package com.imperial.heap3d.implementations.layout;

import java.util.Map.Entry;
import java.util.Set;

import com.graphics.shapes.Colour;
import com.graphics.shapes.Shape;

public class InsideObject {

	private IRenderEngine _renderEngine;
	private float[] _currentPos;
	private boolean entered;
	private Shape s;
	private Set<Entry<String, Object>> _primitives;
	private float incX;
	
	public InsideObject(IRenderEngine renderEngine, Shape shape, float[] currentPos, Set<Entry<String, Object>> primitives){
		_renderEngine = renderEngine;
		_currentPos = currentPos;
		s = shape;
		_primitives = primitives;
	}
	
	public void enter(){
		
		float[] pos = s.getPosition();
		
		if (!entered && Math.round(_renderEngine.getCameraPos()[0]) == Math.round(s.getPosition()[0])
				&& Math.round(_renderEngine.getCameraPos()[1]) == Math.round(s.getPosition()[1])
				&& Math.round(_renderEngine.getCameraPos()[2]) == Math.round(s.getPosition()[2])) {
			//We have reached target
			entered = true;
			Colour c = s.getColour();
			_renderEngine.setBackgroundColour(c.getR(), c.getG(), c.getB(), 1);
			_renderEngine.switchActiveLayer(1);
			
			if(_primitives == null)
				return;
			
			for(Entry<String, Object> e : _primitives){
				_renderEngine.printTo3DSpace(pos[0]+(incX+=5), pos[1], pos[2]-10, 0, 0, 0, 0.1f, e.getKey() + " [] " + e.getValue());
			}
		}
		
	}
	
	public void exit(){
		float[] pos = s.getPosition();
		_renderEngine.setCameraPosition(pos[0], pos[1], pos[2]);
		_renderEngine.setCameraPositionSmooth(_currentPos[0], _currentPos[1], _currentPos[2]);
	}
	
}
