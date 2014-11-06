package com.heap3d.application.events;

/**
 * Created by oskar on 06/11/14.
 */
public class StartDefinition {

    public final String className;
    public final String command;
    public final int port;

    public StartDefinition(String className, String command, int port) {
        this.className = className;
        this.command = command;
        this.port = port;
    }
}
