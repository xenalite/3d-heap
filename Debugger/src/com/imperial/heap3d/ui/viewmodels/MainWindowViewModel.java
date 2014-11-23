package com.imperial.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.application.ControlEventHandler;
import com.imperial.heap3d.events.ControlEventFactory;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.utilities.ICommand;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.utilities.RelayCommand;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.imperial.heap3d.events.EventType.*;

/**
 * Created by oskar on 29/10/14.
 */
public class MainWindowViewModel {

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
    private SimpleStringProperty _debugeeOutput;
    private SimpleStringProperty _debugeeInput;
    private StringProperty _jvmArguments;
    private StringProperty _variables;

    public MainWindowViewModel(EventBus eventBus, IVirtualMachineProvider VMProvider) {
        _VMProvider = VMProvider;
        _eventBus = eventBus;
        _eventBus.register(this);

        _startActionCommand = new RelayCommand(this::startAction);
        _startActionCommand.canExecute().set(true);
        _stopActionCommand = new RelayCommand(this::stopAction);
        _pauseActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(PAUSE)));
        _resumeActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(RESUME)));
        _stepActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEP)));

        _className = new SimpleStringProperty(this, "", "test_programs.small_stack.Program");
        _classPath = new SimpleStringProperty(this, "", System.getProperty("user.home") + "/workspace/3d-heap/Debugger/out/production/Debugger/");
        _javaPath = new SimpleStringProperty(this, "", System.getProperty("java.home") + "/bin/java");
        _status = new SimpleStringProperty(this, "status", "");
        _debugeeOutput = new SimpleStringProperty("this", "", "");
        _debugeeInput = new SimpleStringProperty("this", "", "");
        _jvmArguments = new SimpleStringProperty(this, "", "");
        _variables = new SimpleStringProperty(this, "","");
    }

    @Subscribe
    public void handleProcessStopped(ProcessEvent pe) {
        try {
            switch (pe.type) {
                case STARTED: {

                }
                break;
                case STOPPED: {
                    Platform.runLater(this::setButtonsOnStop);
                }
                break;
                case DEBUG_MSG: {
                    //TODO these runlater should be removed
                    Platform.runLater(() -> _variables.set(pe.message));
                }
                break;
                case PROCESS_MSG: {
                    Platform.runLater(() -> _debugeeOutput.set(_debugeeOutput.get()
                            + System.lineSeparator() + pe.message));
                }
                break;
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
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
        _debugeeInput.set("");
        _debugeeOutput.set("");
        _startActionCommand.canExecute().set(false);
        _stopActionCommand.canExecute().set(true);
        _stepActionCommand.canExecute().set(true);
        _pauseActionCommand.canExecute().set(true);
        _resumeActionCommand.canExecute().set(true);

        String jvmFormat = "-agentlib:jdwp=transport=dt_socket,address=%d,server=n,suspend=y";

        StartDefinition sd = new StartDefinition(_javaPath.get(), _className.get(), jvmFormat, _classPath.get());
        ControlEventHandler handler = new ControlEventHandler(sd, _VMProvider, _eventBus);
        _eventBus.post(ControlEventFactory.createEventOfType(START));

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                handler.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

    //region Properties
    public StringProperty getJvmArgumentsProperty() {
        return _jvmArguments;
    }

    public StringProperty getJavaPathProperty() { return _javaPath; }

    public StringProperty getClassPathProperty() {
        return _classPath;
    }

    public StringProperty getClassNameProperty() {
        return _className;
    }

    public StringProperty getStatusProperty() {
        return _status;
    }

    public StringProperty getDebugeeOutputProperty() { return _debugeeOutput; }

    public StringProperty getDebugeeInputProperty() { return _debugeeInput; }

    public StringProperty getVariablesProperty() {
        return _variables;
    }

    public ICommand getPauseActionCommand() { return _pauseActionCommand; }

    public ICommand getResumeActionCommand() { return _resumeActionCommand; }

    public ICommand getStepActionCommand() { return _stepActionCommand; }

    public ICommand getStartActionCommand() { return _startActionCommand; }

    public ICommand getStopActionCommand() { return _stopActionCommand; }
    //endregion
}