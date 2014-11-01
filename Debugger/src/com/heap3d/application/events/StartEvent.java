package com.heap3d.application.events;

/**
 * Created by oskar on 01/11/14.
 */
public class StartEvent implements IEvent<StartDefinition> {

    private final StartDefinition _definition;

    public StartEvent(StartDefinition definition) {
        _definition = definition;
    }

    @Override
    public StartDefinition getContent() {
        return _definition;
    }
}
