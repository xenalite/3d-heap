package tests.system.snapshotTest;

import com.imperial.heap3d.implementations.factories.ProcessFactory;
import javafx.embed.swing.JFXPanel;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.factories.HeapGraphFactory;
import com.imperial.heap3d.implementations.factories.VirtualMachineProvider;
import com.imperial.heap3d.implementations.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.implementations.viewmodels.BreakpointsTabViewModel;

public class DebuggerController {

    private ApplicationTabViewModel _applicationViewModel;
    private BreakpointsTabViewModel _breakpointsVM;
    private HeapGraphFactory _heapGraphFactory;
    private Thread _renderThread;
    
    public DebuggerController() {
    	new JFXPanel();
        _heapGraphFactory = new HeapGraphFactory();
        _renderThread = new Thread(_heapGraphFactory.create(), "lwjgl");
        _renderThread.start();
        EventBus e = new EventBus();
        setBreakpointsVM(new BreakpointsTabViewModel(e));
        setApplicationVM(new ApplicationTabViewModel(e,
                new ProcessFactory(new VirtualMachineProvider(), e, _heapGraphFactory)));
    }
    
    public void cleanup(){
    	_heapGraphFactory.create().finish();
    	try {
			_renderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	_renderThread = null;
    	_heapGraphFactory = null;
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
