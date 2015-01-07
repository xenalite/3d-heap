package com.heap3d.implementations.events;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ControlEvent &&
                type == ((ControlEvent) o).type &&
                Objects.equals(className, ((ControlEvent) o).className) &&
                Objects.equals(argument, ((ControlEvent) o).argument);
    }
}
