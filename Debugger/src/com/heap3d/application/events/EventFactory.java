package com.heap3d.application.events;

import com.heap3d.application.utilities.BreakpointDefinition;

/**
 * Created by oskar on 01/11/14.
 */
public class EventFactory {

    public static IEvent<BreakpointDefinition> createNewBreakpointEvent(BreakpointDefinition definition) {
        return new NewBreakpointEvent(definition);
    }

    public static IEvent<StartDefinition> createNewStartDefinition(StartDefinition definition) {
        return new StartEvent(definition);
    }
}
