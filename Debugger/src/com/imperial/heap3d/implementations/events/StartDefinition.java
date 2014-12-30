package com.imperial.heap3d.implementations.events;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.application.StreamListener;
import com.imperial.heap3d.implementations.factories.ThreadBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created by oskar on 06/11/14.
 */
public class StartDefinition {

    private final String javaPath;
    private final String className;
    private final String classpath;
    private final String classArgs;

    public StartDefinition(String javaPath, String className, String classpath, String classArgs) {
        this.javaPath = javaPath;
        this.className = className;
        this.classpath = classpath;
        this.classArgs = classArgs;
    }

    public Process buildProcess(int port, EventBus eventBus) throws IOException {
        final String format = "-agentlib:jdwp=transport=dt_socket,address=%d,server=n,suspend=y";
        String debugArgument = String.format(format, port);
        ProcessBuilder pb = new ProcessBuilder(javaPath, debugArgument, "-cp", classpath, className, classArgs);
        Process p = pb.start();

        ExecutorService service = ThreadBuilder.createService("stream-listener");
        service.submit(new StreamListener(eventBus, p.getInputStream(), p.getErrorStream()));
        service.shutdown();

        return p;
    }
}
