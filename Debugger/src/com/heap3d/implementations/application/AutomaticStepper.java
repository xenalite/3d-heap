package com.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.implementations.events.ControlEventFactory;
import com.heap3d.implementations.events.EventType;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.ProcessEventType;
import com.sun.jdi.event.LocatableEvent;

public class AutomaticStepper implements Runnable{

	private long _sleepTime;
	private EventBus _eventBus;
	
	public AutomaticStepper(long sleepTime, EventBus eventBus){
		_sleepTime = sleepTime;
		_eventBus = eventBus;
		_eventBus.register(this);
	}

	@Subscribe
	public void handleProcessEvent(ProcessEvent event) {
		if(event.type == ProcessEventType.DEBUG_MSG) {
			LocatableEvent levent = event.event;
			// WATCH OUT FOR MULTI THREADING
		}
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
