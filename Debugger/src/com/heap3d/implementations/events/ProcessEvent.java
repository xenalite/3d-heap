package com.heap3d.implementations.events;

import com.sun.jdi.event.LocatableEvent;

/**
 * Created by oskar on 11/11/14.
 */
public class ProcessEvent {

    public final ProcessEventType type;
    public final String message;
    public final LocatableEvent event;

    public ProcessEvent(ProcessEventType type) {
        this.type = type;
        this.message = null;
        this.event = null;
    }

    public ProcessEvent(ProcessEventType type, String message) {
        this.type = type;
        this.message = message;
        this.event = null;
    }

    public ProcessEvent(ProcessEventType type, LocatableEvent event) {
        this.type = type;
        this.message = null;
        this.event = event;
    }
}
