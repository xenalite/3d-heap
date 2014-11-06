package com.heap3d.application.events;

/**
 * Created by oskar on 01/11/14.
 */
public class EventUtils {

    public static EventDTO createControlEvent(EventType type) {
        return new EventDTO(type, null, null);
    }

    public static EventDTO createBreakpointEvent(String className, String methodName) {
        return new EventDTO(EventType.BREAKPOINT, className, methodName);
    }

    public static EventDTO createWatchpointEvent(String className, String fieldName) {
        return new EventDTO(EventType.WATCHPOINT, className, fieldName);
    }
}
