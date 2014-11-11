package com.heap3d.application.events;

import com.heap3d.application.utilities.StreamPipe;

import java.io.IOException;

/**
 * Created by oskar on 06/11/14.
 */
public class StartDefinition {

    public final String javaPath;
    public final String className;
    public final String jvmArgumentFormat;
    public final String classpath;
    public final StreamPipe _debugeeOutputStreamPipe;

    public StartDefinition(String javaPath, String className, String jvmArgumentFormat, String classpath, StreamPipe debugeeOutputStreamPipe) {
        this.javaPath = javaPath;
        this.className = className;
        this.jvmArgumentFormat = jvmArgumentFormat;
        this.classpath = classpath;
        this._debugeeOutputStreamPipe = debugeeOutputStreamPipe;
    }

    public Process buildProcess(int port) throws IOException {
        String jvmArgument = String.format(jvmArgumentFormat, port);
        ProcessBuilder pb = new ProcessBuilder(javaPath, jvmArgument, "-cp", classpath, className);
        Process p = pb.start();
        _debugeeOutputStreamPipe.setInputStream(p.getInputStream());
        new Thread(_debugeeOutputStreamPipe).start();
        return p;
    }
}
