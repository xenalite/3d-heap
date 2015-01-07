package com.heap3d.implementations.render;

import com.google.common.eventbus.EventBus;
import com.graphics.userinput.Input;
import com.heap3d.implementations.events.ControlEventFactory;
import com.heap3d.implementations.events.EventType;
import com.heap3d.interfaces.render.IRenderEngine;

public class UserInput {

	private IRenderEngine _renderEngine;
	private EventBus _eventBus;
	
	public UserInput(IRenderEngine renderEngine, EventBus eventBus) {
        _renderEngine = renderEngine;
        _eventBus = eventBus;
	}
	
	public void inLoop(){
		
		Input userInput = _renderEngine.getInput();
		
		if(userInput == null)
			return;
		
		if(userInput.hasStarted()){
			System.out.println("Command: Start");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.START));
		}else if(userInput.hasStopped()){
			System.out.println("Command: Stop");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.STOP));
		}else if(userInput.hasPaused()){
			System.out.println("Command: Pause");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.PAUSE));
		}else if(userInput.hasResumed()){
			System.out.println("Command: Resume");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.RESUME));
		}else if(userInput.hasStepedOver()){
			System.out.println("Command: Step Over");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.STEPOVER));
		}else if(userInput.hasStepedInto()){
			System.out.println("Command: Step Into");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.STEPINTO));
		}else if(userInput.hasStepedOut()){
			System.out.println("Command: Step Out");
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.STEPOUT));
		}
		
	}
	
}
