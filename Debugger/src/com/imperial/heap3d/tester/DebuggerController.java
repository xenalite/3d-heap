package com.imperial.heap3d.tester;
import EDU.oswego.cs.dl.util.concurrent.Heap;
import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import java.awt.*;

/**
 * Created by Robert on 23/11/14.
 */

public class DebuggerController {

    private ApplicationTabViewModel _windowVM;
    private BreakpointsTabViewModel _breakpointsVM;

    public DebuggerController(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        Canvas canvas = new Canvas();
        panel.add(canvas);
        frame.add(panel);
        frame.setSize(1200, 720);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        HeapGraphFactory heapGraphFactory = new HeapGraphFactory(canvas);
        EventBus e = new EventBus();
        setBreakpointsVM(new BreakpointsTabViewModel(e));
        setWindowVM(new ApplicationTabViewModel(e, new VirtualMachineProvider(), heapGraphFactory));
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
        _windowVM.getStepOverActionCommand().execute();
    }
    public void screenShot(String path){
        _windowVM.getScreenShotPath().setValue(path);
        _windowVM.getScreenShotActionCommand().execute();
    }

}
