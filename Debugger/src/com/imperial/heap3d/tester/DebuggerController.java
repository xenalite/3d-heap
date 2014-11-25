package com.imperial.heap3d.tester;
import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Robert on 23/11/14.
 */

public class DebuggerController {

    private JFrame frame;

    private ApplicationTabViewModel _applicationViewModel;
    private BreakpointsTabViewModel _breakpointsVM;

    public DebuggerController() {
//        frame = new JFrame();
//        JPanel panel = new JPanel();
        Canvas canvas = new Canvas();
//        panel.add(canvas);
//        frame.add(panel);
//        frame.setSize(1200, 720);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        HeapGraphFactory heapGraphFactory = new HeapGraphFactory(canvas);
        EventBus e = new EventBus();
        setBreakpointsVM(new BreakpointsTabViewModel(e));
        setApplicationVM(new ApplicationTabViewModel(e, new VirtualMachineProvider(), heapGraphFactory));
    }

    public void setApplicationVM(ApplicationTabViewModel windowVM) { this._applicationViewModel = windowVM; }

    public void setBreakpointsVM(BreakpointsTabViewModel breakpointsVM) {
        this._breakpointsVM = breakpointsVM;
    }

    public void setJavaPath(String javaPath) {
        _applicationViewModel.getJavaPathProperty().setValue(javaPath);
    }

    public void setClassPath(String classPath) {
        _applicationViewModel.getClassPathProperty().setValue(classPath);
    }

    public void setClassName(String className) {
        _applicationViewModel.getClassNameProperty().setValue(className);
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
        _applicationViewModel.getStartActionCommand().execute();
    }
    public void step(){
        _applicationViewModel.getStepOverActionCommand().execute();
    }
    public void screenShot(String path){
        _applicationViewModel.getScreenShotPath().setValue(path);
        _applicationViewModel.getScreenShotActionCommand().execute();
    }

    public void stop() {
        _applicationViewModel.getStopActionCommand().execute();
    }
}
