package tests.system.snapshotTest;

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