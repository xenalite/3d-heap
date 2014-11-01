package org.debugger.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import javafx.beans.property.*;
import org.debugger.application.utilities.CommandBuilder;
import org.debugger.application.utilities.EventHandler;
import org.debugger.application.utilities.IVirtualMachineProvider;

import java.io.IOException;

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
    private BooleanProperty _disableStart;
    private Process _process;

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
        _disableStart = new SimpleBooleanProperty(this, "disableStart", false);
    }

    public BooleanProperty getDisableStart() {
        return _disableStart;
    }

    public StringProperty getSourceCodeProperty() {
        return _sourceCode;
    }

    public void stopAction() {
        _process.destroy();
        _status.set("NOT RUNNING");
        _disableStart.set(false);
    }

    public void startAction() {
        _status.set("RUNNING");

        _disableStart.set(true);
        try {
            _process = Runtime.getRuntime().exec(
                    CommandBuilder.buildCommand(_jdkPath.get(), _classPath.get(), _className.get(), _port.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
