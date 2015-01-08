package com.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.heap3d.implementations.events.ControlEventFactory;
import com.heap3d.implementations.events.EventType;

public class AutomaticStepper implements Runnable{

	private long _sleepTime;
	private EventBus _eventBus;
	
	public AutomaticStepper(long sleepTime, EventBus eventBus){
		_sleepTime = sleepTime;
		_eventBus = eventBus;
	}

	@Override
	public void run() {
		
		while(true){
			
			_eventBus.post(ControlEventFactory.createEventOfType(EventType.STEPINTO));

			try {
				Thread.sleep(_sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
