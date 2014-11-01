package com.heap3d.application.utilities;

/**
 * Created by oskar on 31/10/14.
 */
public class CommandBuilder {

    public static String buildCommand(String jdk, String path, String className, int port) {
        StringBuilder sb = new StringBuilder();
        sb.append(jdk);
        sb.append("/bin/java ");
        sb.append("-agentlib:jdwp=transport=dt_socket,address=");
        sb.append(port);
        sb.append(",server=y,suspend=y -cp ");
        sb.append(path);
        sb.append(" ");
        sb.append(className);

        return sb.toString();
    }
}
