package com.imperial.heap3d.tester;


/**
 * Created by zhouyou_robert on 23/11/14.
 */
public class DebuggerControllerBuilder{
    private DebuggerController result;

    public DebuggerControllerBuilder(){
        result = new DebuggerController();
    }
    public DebuggerControllerBuilder setClassPath(String classPath){
        result.setClassPath(classPath);
        return this;
    }

    public DebuggerControllerBuilder setJavaPath(String javaPath) {
        result.setJavaPath(javaPath);
        return this;
    }

    public DebuggerControllerBuilder setClassName(String className) {
        result.setClassName(className);
        return this;
    }

    public DebuggerController getDebuggerController(){
        return result;
    }

}