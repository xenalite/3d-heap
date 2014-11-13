package com.imperial.heap3d.events;

import java.io.IOException;

/**
 * Created by oskar on 06/11/14.
 */
public class StartDefinition {

    public final String javaPath;
    public final String className;
    public final String jvmArgumentFormat;
    public final String classpath;

    public StartDefinition(String javaPath, String className, String jvmArgumentFormat, String classpath) {
        this.javaPath = javaPath;
        this.className = className;
        this.jvmArgumentFormat = jvmArgumentFormat;
        this.classpath = classpath;
    }

    public Process buildProcess(int port) throws IOException {
        String jvmArgument = String.format(jvmArgumentFormat, port);
        ProcessBuilder pb = new ProcessBuilder(javaPath, jvmArgument, "-cp", classpath, className);
        Process p = pb.start();
        return p;
    }
}
