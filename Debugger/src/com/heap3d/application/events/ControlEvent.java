package com.heap3d.application.events;

import com.heap3d.application.events.definitions.EventType;

/**
 * Created by oskar on 02/11/14.
 */
public class ControlEvent implements IEvent<EventType> {

    private final EventType _eventType;

    public ControlEvent(EventType eventType) {
        _eventType = eventType;
    }

    @Override
    public EventType getContent() {
        return _eventType;
    }
}
