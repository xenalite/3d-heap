package com.heap3d.implementations.events;

/**
 * Created by oskar on 01/11/14.
 */
public class ControlEventFactory {

    public static ControlEvent createEventOfType(EventType type) {
        return new ControlEvent(type, null, null);
    }
    public static ControlEvent createScreenShotEvent(String path) {
        return new ControlEvent(EventType.SCREENSHOT, null, path);
    }
    public static ControlEvent createBreakpointEvent(String className, String methodName) {
        return new ControlEvent(EventType.BREAKPOINT, className, methodName);
    }
    public static ControlEvent createRemoveBreakpointEvent(String className, String methodName) {
        return new ControlEvent(EventType.RMBREAKPOINT, className, methodName);
    }
    public static ControlEvent createWatchpointEvent(String className, String fieldName) {
        return new ControlEvent(EventType.WATCHPOINT, className, fieldName);
    }
    public static ControlEvent createRemoveWatchpointEvent(String className, String methodName) {
        return new ControlEvent(EventType.RMWATCHPOINT, className, methodName);
    }
}
