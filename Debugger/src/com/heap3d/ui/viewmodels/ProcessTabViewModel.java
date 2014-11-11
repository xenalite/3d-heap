package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.EventHandler;
import com.heap3d.application.events.ControlEventFactory;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.EventHandlerFactory;
import com.heap3d.application.utilities.StreamPipe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.heap3d.application.utilities.ICommand;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.heap3d.application.utilities.ProcessState;
import com.heap3d.application.utilities.RelayCommand;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heap3d.application.events.EventType.*;
import static com.heap3d.application.utilities.ProcessState.STOPPED;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabViewModel {

    private EventBus _eventBus;
    private IVirtualMachineProvider _VMProvider;

    private ICommand _resumeActionCommand;
    private ICommand _pauseActionCommand;
    private ICommand _stepActionCommand;
    private ICommand _startActionCommand;
    private ICommand _stopActionCommand;

    private StringProperty _status;
    private StringProperty _javaPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private SimpleStringProperty _debuggerOutput;
    private SimpleStringProperty _debuggeeOutput;
    private StringProperty _jvmArgs;
    private BooleanProperty _enableButtons;
    private StreamPipe _debugeeOutputStreamPipe;
    private StringProperty _jvmArguments;

    public ProcessTabViewModel(EventBus eventBus, IVirtualMachineProvider VMProvider) {
        _VMProvider = VMProvider;
        _eventBus = eventBus;
        _eventBus.register(this);

        _startActionCommand = new RelayCommand(this::startAction);
        _startActionCommand.canExecute().set(true);
        _stopActionCommand = new RelayCommand(this::stopAction);
        _pauseActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(PAUSE)));
        _resumeActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(RESUME)));
        _stepActionCommand = new RelayCommand(() -> {});

        _className = new SimpleStringProperty(this, "className", "com.heap3d.application.Debugee");
        _classPath = new SimpleStringProperty(this, "classpath", System.getProperty("user.home") + "/workspace/3d-heap/Debugger/out/production/Debugger/");
        _javaPath = new SimpleStringProperty(this, "jdkPath", System.getProperty("java.home") + "/bin/java");
        _status = new SimpleStringProperty(this, "status", "NOT RUNNING");
        _debuggerOutput = new SimpleStringProperty("this", "debuggerOutput", "");
        _debuggeeOutput = new SimpleStringProperty("this", "debuggeeOutput", "");
        _debugeeOutputStreamPipe = new StreamPipe(_debuggeeOutput);
        _jvmArgs = new SimpleStringProperty(this, "jvmArgs", "");
        _enableButtons = new SimpleBooleanProperty(this, "disableStart", true);
        _jvmArguments = new SimpleStringProperty(this, "jvmArgs", "");
    }

    @Subscribe
    public void handleProcessStopped(ProcessState s) {
        if(s == STOPPED)
            Platform.runLater(this::setButtonsOnStop);
    }

    private void setButtonsOnStop() {
        _status.set("STOPPED");
        _startActionCommand.canExecute().set(true);
        _stopActionCommand.canExecute().set(false);
        _stepActionCommand.canExecute().set(false);
        _pauseActionCommand.canExecute().set(false);
        _resumeActionCommand.canExecute().set(false);
    }

    private void stopAction() {
        setButtonsOnStop();
        _eventBus.post(ControlEventFactory.createEventOfType(STOP));
    }

    private void startAction() {
        _status.set("RUNNING");
        _startActionCommand.canExecute().set(false);
        _stopActionCommand.canExecute().set(true);
        _stepActionCommand.canExecute().set(true);
        _pauseActionCommand.canExecute().set(true);
        _resumeActionCommand.canExecute().set(true);

        String jvmFormat = "-agentlib:jdwp=transport=dt_socket,address=%d,server=n,suspend=y";

        StartDefinition sd = new StartDefinition(_javaPath.get(), _className.get(), jvmFormat, _classPath.get(), _debugeeOutputStreamPipe);
        EventHandler handler = new EventHandler(sd, _VMProvider, _eventBus);
        _eventBus.post(ControlEventFactory.createEventOfType(START));

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                handler.run();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

    @Subscribe
    public void handleDebuggerOutput(String s) {
        Platform.runLater(() -> appendToOutput(s));
    }

    private void appendToOutput(Object value) {
        _debuggerOutput.set(_debuggerOutput.get() + System.lineSeparator() + value);
    }

    //region Properties
    public StringProperty getJvmArgs() {
        return _jvmArguments;
    }

    public StringProperty getJavaPath() { return _javaPath; }

    public StringProperty getClassPath() {
        return _classPath;
    }

    public StringProperty getClassName() {
        return _className;
    }

    public StringProperty getStatus() {
        return _status;
    }

    public StringProperty getDebuggerOutput() { return _debuggerOutput; }
    public StringProperty getDebuggeeOutput() { return _debuggeeOutput; }

    public ICommand getPauseActionCommand() { return _pauseActionCommand; }

    public ICommand getResumeActionCommand() { return _resumeActionCommand; }

    public ICommand getStepActionCommand() { return _stepActionCommand; }

    public ICommand getStartActionCommand() { return _startActionCommand; }

    public ICommand getStopActionCommand() { return _stopActionCommand; }
    //endregion
}