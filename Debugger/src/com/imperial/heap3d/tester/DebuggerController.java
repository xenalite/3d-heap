package com.imperial.heap3d.tester;
import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;

/**
 * Created by Robert on 23/11/14.
 */

public class DebuggerController {

    private ApplicationTabViewModel _windowVM;
    private BreakpointsTabViewModel _breakpointsVM;

    public DebuggerController(){
        EventBus e = new EventBus();
        setBreakpointsVM(new BreakpointsTabViewModel(e));
        setWindowVM(new ApplicationTabViewModel(e, new VirtualMachineProvider()));
    }

    public void setWindowVM(ApplicationTabViewModel windowVM) { this._windowVM = windowVM; }

    public void setBreakpointsVM(BreakpointsTabViewModel breakpointsVM) {
        this._breakpointsVM = breakpointsVM;
    }



    public void setJavaPath(String javaPath) {
        _windowVM.getJavaPathProperty().setValue(javaPath);
    }

    public void setClassPath(String classPath) {
        _windowVM.getClassPathProperty().setValue(classPath);
    }

    public void setClassName(String className) {
        _windowVM.getClassNameProperty().setValue(className);
    }

    public void addBreakpoint(String className, String methodName){
        _breakpointsVM.getBreakpointClassProperty().setValue(className);
        _breakpointsVM.getBreakpointMethodProperty().setValue(methodName);
        _breakpointsVM.addBreakpointAction();
    }
    public void addWatchpoint(String className, String fieldName){
        _breakpointsVM.getWatchpointClassProperty().setValue(className);
        _breakpointsVM.getWatchpointFieldProperty().setValue(fieldName);
        _breakpointsVM.addWatchpointAction();
    }

    public void start(){
        _windowVM.getStartActionCommand().execute();
    }
    public void step(){
        _windowVM.getStepIntoActionCommand().execute();
    }

}
