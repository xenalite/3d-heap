package com.heap3d.application.utilities;

/**
 * Created by oskar on 01/11/14.
 */
public class BreakpointDefinition {

    private final String _className;
    private final String _methodName;
    private final int _lineNumber;

    public BreakpointDefinition(String className, String methodName, int lineNumber) {
        _className = className;
        _methodName = methodName;
        _lineNumber = lineNumber;
    }

    public String getClassName() {
        return _className;
    }

    public String getMethodName() {
        return _methodName;
    }

    public int getLineNumber() {
        return _lineNumber;
    }
}
