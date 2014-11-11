package com.heap3d.application.events;

/**
 * Created by oskar on 01/11/14.
 */
public class ControlEventFactory {

    public static ControlEvent createEventOfType(EventType type) {
        return new ControlEvent(type, null, null);
    }

    public static ControlEvent createBreakpointEvent(String className, String methodName) {
        return new ControlEvent(EventType.BREAKPOINT, className, methodName);
    }

    public static ControlEvent createWatchpointEvent(String className, String fieldName) {
        return new ControlEvent(EventType.WATCHPOINT, className, fieldName);
    }
}
