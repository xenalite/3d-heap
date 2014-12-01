package com.imperial.heap3d.implementations.events;

import java.io.IOException;

/**
 * Created by oskar on 06/11/14.
 */
public class StartDefinition {

    private final String javaPath;
    private final String className;
    private final String classpath;

    public StartDefinition(String javaPath, String className, String classpath) {
        this.javaPath = javaPath;
        this.className = className;
        this.classpath = classpath;
    }

    public Process buildProcess(int port) throws IOException {
        final String format = "-agentlib:jdwp=transport=dt_socket,address=%d,server=n,suspend=y";
        String debugArgument = String.format(format, port);
        return new ProcessBuilder(javaPath, debugArgument, "-cp", classpath, className).start();
    }
}
