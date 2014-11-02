package com.heap3d.application.events;

import com.heap3d.application.events.definitions.BreakpointDefinition;

/**
 * Created by oskar on 01/11/14.
 */
public class NewBreakpointEvent implements IEvent<BreakpointDefinition> {

    private final BreakpointDefinition _definition;

    public NewBreakpointEvent(BreakpointDefinition definition) {
        _definition = definition;
    }

    @Override
    public BreakpointDefinition getContent() {
        return _definition;
    }
}
