package org.debugger.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.debugger.application.utilities.EventHandler;
import org.debugger.application.utilities.IVirtualMachineProvider;
import javafx.beans.property.*;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabViewModel {

    private static final int DEFAULT_PORT = 8000;

    private EventBus _eventBus;
    private IVirtualMachineProvider _virtualMachineProvider;
    private EventHandler _eventHandler;
    private StringProperty _status;
    private StringProperty _jdkPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private IntegerProperty _port;
    private StringProperty _sourceCode;

    public ProcessTabViewModel(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus,
                               EventHandler eventHandler) {
        _eventHandler = eventHandler;

        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _eventBus.register(this);
        _className = new SimpleStringProperty(this, "className", "org.debugger.application.Debugee");
        _classPath = new SimpleStringProperty(this, "classpath", "~/workspace/3d-heap/Debugger/out/production/Debugger/");
        _jdkPath = new SimpleStringProperty(this, "jdkPath", System.getProperty("java.home"));
        _status = new SimpleStringProperty(this, "status", "NOT RUNNING");
        _port = new SimpleIntegerProperty(this, "port", DEFAULT_PORT);
        _sourceCode = new SimpleStringProperty(this, "SourceCode", "No source attached");
    }

    public StringProperty getSourceCodeProperty() {
        return _sourceCode;
    }

    public void stopAction() {
        _status.set("NOT RUNNING");
    }

    public void startAction() {
        _status.set("RUNNING");
        StringBuilder sb = new StringBuilder();
        sb.append(_jdkPath.get());
        sb.append(System.getProperty("file.separator"));
        sb.append("bin");
        sb.append(System.getProperty("file.separator"));
        sb.append("java");
        sb.append(" ");
        sb.append("-agentlib:jdwp=transport=dt_socket,address=");
        sb.append(_port.get());
        sb.append(",server=y,suspend=y");
        sb.append(" -cp ");
        sb.append(_classPath.get());
        sb.append(" ");
        sb.append(_className.get());

        try {
            Runtime.getRuntime().exec(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.submit(() -> _eventHandler.runEventLoop(_port.get()));
//        _eventBus.post(_port.get());
//        notifyAll();
//        _virtualMachineProvider.createAtPort(_port.get());
//        System.out.println("...");
//        _eventBus.post(new ChangeEvent(new MyEvent("event happened")));
//        _eventBus.post(new MyEvent("my event happened"));
    }

    @Subscribe
    public void handler2(MyEvent e) { System.out.println(e.getContent()); }

    @Subscribe
    public void handler(Object e) {
        System.out.println(e);
    }

    public StringProperty getJdkPathProperty() {
        return _jdkPath;
    }

    public StringProperty getClassPathProperty() {
        return _classPath;
    }

    public StringProperty getClassNameProperty() {
        return _className;
    }

    public StringProperty getStatusProperty() {
        return _status;
    }

    public IntegerProperty getPortProperty() {
        return _port;
    }
}
