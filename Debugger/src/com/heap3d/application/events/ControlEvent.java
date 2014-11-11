package com.heap3d.application.events;

/**
 * Created by oskar on 04/11/14.
 */
public class ControlEvent {

    public final EventType type;
    public final String className;
    public final String argument;

    public ControlEvent(EventType type, String className, String argument) {
        this.type = type;
        this.className = className;
        this.argument = argument;
    }
}
