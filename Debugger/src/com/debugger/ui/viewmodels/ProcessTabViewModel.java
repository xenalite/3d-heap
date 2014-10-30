package com.debugger.ui.viewmodels;

import com.debugger.application.utilities.IVirtualMachineProvider;
import javafx.beans.property.*;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabViewModel {

    private static final int DEFAULT_PORT = 8000;

    private IVirtualMachineProvider _virtualMachineProvider;
    private StringProperty _status;
    private StringProperty _jdkPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private IntegerProperty _port;
    private StringProperty _sourceCode;

    public ProcessTabViewModel(IVirtualMachineProvider virtualMachineProvider) {
        _virtualMachineProvider = virtualMachineProvider;
        _className = new SimpleStringProperty(this, "className", "");
        _classPath = new SimpleStringProperty(this, "classpath", "");
        _jdkPath = new SimpleStringProperty(this, "jdkPath", System.getProperty("java.home"));
        _status = new SimpleStringProperty(this, "status", "NOT RUNNING");
        _port = new SimpleIntegerProperty(this, "port", DEFAULT_PORT);
        _sourceCode = new SimpleStringProperty(this, "SourceCode", "No source attached");
    }

    public String getSourceCode() {
        System.out.println("get");
        return _sourceCode.get();
    }

    public void setSourceCode(String value) {
        System.out.println("set");
        _sourceCode.set(value);
    }

    public StringProperty getSourceCodeProperty() {
        return _sourceCode;
    }

    public void stopAction() {
        _status.set("NOT RUNNING");
    }

    public void startAction() {
        _status.set("RUNNING");
        _virtualMachineProvider.createAtPort(_port.get());
        System.out.println("...");
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
