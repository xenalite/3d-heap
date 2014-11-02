package com.heap3d.application.events;

import com.heap3d.application.events.definitions.BreakpointDefinition;
import com.heap3d.application.events.definitions.EventType;
import com.heap3d.application.events.definitions.StartDefinition;

/**
 * Created by oskar on 01/11/14.
 */
public class EventUtils {

    public static IEvent<BreakpointDefinition> createNewBreakpointEvent(BreakpointDefinition definition) {
        return new NewBreakpointEvent(definition);
    }

    public static IEvent<StartDefinition> createNewStartEvent(StartDefinition definition) {
        return new StartEvent(definition);
    }

    public static IEvent<EventType> createNewDestroyEvent() {
        return new ControlEvent(EventType.STOP);
    }

    public static IEvent<EventType> createNewResumeEvent() {
        return new ControlEvent(EventType.RESUME);
    }

    public static IEvent<EventType> createNewPauseEvent() {
        return new ControlEvent(EventType.PAUSE);
    }
}
