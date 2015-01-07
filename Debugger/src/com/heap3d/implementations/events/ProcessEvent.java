package com.heap3d.implementations.events;

/**
 * Created by oskar on 11/11/14.
 */
public class ProcessEvent {

    public final ProcessEventType type;
    public final String message;

    public ProcessEvent(ProcessEventType type) {
        this.type = type;
        this.message = null;
    }

    public ProcessEvent(ProcessEventType type, String message) {
        this.type = type;
        this.message = message;
    }
}
