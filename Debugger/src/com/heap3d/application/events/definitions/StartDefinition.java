package com.heap3d.application.events.definitions;

/**
 * Created by oskar on 01/11/14.
 */
public class StartDefinition {

    private final String _jdk;
    private final String _path;
    private final String _name;
    private final int _port;

    public StartDefinition(String jdk, String path, String name, int port) {
        _jdk = jdk;
        _path = path;
        _name = name;
        _port = port;
    }

    public String getJdk() {
        return _jdk;
    }

    public String getPath() {
        return _path;
    }

    public String getName() {
        return _name;
    }

    public int getPort() {
        return _port;
    }
}
