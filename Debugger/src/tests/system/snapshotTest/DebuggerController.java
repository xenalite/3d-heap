package tests.system.snapshotTest;

import javafx.embed.swing.JFXPanel;
import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;

public class DebuggerController {

    private ApplicationTabViewModel _applicationViewModel;
    private BreakpointsTabViewModel _breakpointsVM;

    public DebuggerController() {
    	new JFXPanel();
        HeapGraphFactory heapGraphFactory = new HeapGraphFactory();
        Thread t = new Thread(heapGraphFactory.create(), "lwjgl");
        t.start();
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
