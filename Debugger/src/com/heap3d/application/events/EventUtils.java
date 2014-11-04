package com.heap3d.application.events;

/**
 * Created by oskar on 01/11/14.
 */
public class EventUtils {

    public static Event createControlEvent(EventType type) {
        return new Event(type, null, null);
    }

    public static Event createStartEvent(String className, String command) {
        return new Event(EventType.START, className, command);
    }

    public static Event createBreakpointEvent(String className, String methodName) {
        return new Event(EventType.BREAKPOINT, className, methodName);
    }

    public static Event createWatchpointEvent(String className, String fieldName) {
        return new Event(EventType.WATCHPOINT, className, fieldName);
    }
}
