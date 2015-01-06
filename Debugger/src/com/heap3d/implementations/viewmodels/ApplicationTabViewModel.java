package com.heap3d.implementations.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.utilities.PathUtils;
import com.heap3d.implementations.application.ControlEventHandler;
import com.heap3d.implementations.events.ControlEventFactory;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.StartDefinition;
import com.heap3d.implementations.factories.ProcessFactory;
import com.heap3d.implementations.factories.ThreadBuilder;
import com.heap3d.utilities.Check;
import com.heap3d.utilities.ICommand;
import com.heap3d.utilities.RelayCommand;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ExecutorService;

import static com.heap3d.implementations.events.EventType.*;

/**
 * Created by oskar on 29/10/14.
 */
public class ApplicationTabViewModel {

    private ProcessFactory _processFactory;
    private EventBus _eventBus;

    private ICommand _resumeActionCommand;
    private ICommand _pauseActionCommand;
    private ICommand _stepOverActionCommand;
    private ICommand _stepIntoActionCommand;
    private ICommand _stepOutActionCommand;
    private ICommand _startActionCommand;
    private ICommand _stopActionCommand;

    private StringProperty _javaPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private StringProperty _processConsole;
    private StringProperty _debugeeInput;
    private StringProperty _arguments;
    private StringProperty _debuggerConsole;

    public ApplicationTabViewModel(EventBus eventBus, ProcessFactory processFactory) {
        _processFactory = Check.notNull(processFactory, "processFactory");
        _eventBus = Check.notNull(eventBus, "eventBus");
        _eventBus.register(this);

        _startActionCommand = new RelayCommand(this::startAction);
        _startActionCommand.canExecute().set(true);
        _stopActionCommand = new RelayCommand(this::stopAction);
        _pauseActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(PAUSE)));
        _resumeActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(RESUME)));
        _stepOverActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPOVER)));
        _stepIntoActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPINTO)));
        _stepOutActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPOUT)));
        _className = new SimpleStringProperty(this, "", PathUtils.TEST_PROGRAM_CLASS_NAME);
        _classPath = new SimpleStringProperty(this, "", PathUtils.TEST_PROGRAM_CLASSPATH);
        _javaPath = new SimpleStringProperty(this, "", System.getProperty("java.home") + "/bin/java");
        _processConsole = new SimpleStringProperty("this", "", "");
        _debugeeInput = new SimpleStringProperty("this", "", "");
        _arguments = new SimpleStringProperty(this, "", "");
        _debuggerConsole = new SimpleStringProperty(this, "", "");
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent pe) {
        switch (pe.type) {
            case STOPPED: {
                Platform.runLater(this::setButtonsOnStop);
            }
            break;
            case DEBUG_MSG: {
                Platform.runLater(() -> _debuggerConsole.set(pe.message));
            }
            break;
            case PROCESS_MSG: {
                Platform.runLater(() -> _processConsole.set(_processConsole.get()
                        + System.lineSeparator() + pe.message));
            }
        }
    }

    private void setButtonsOnStop() {
        _startActionCommand.canExecute().set(true);
        _stopActionCommand.canExecute().set(false);
        _stepOverActionCommand.canExecute().set(false);
        _stepIntoActionCommand.canExecute().set(false);
        _stepOutActionCommand.canExecute().set(false);
        _pauseActionCommand.canExecute().set(false);
        _resumeActionCommand.canExecute().set(false);
    }

    private void stopAction() {
        setButtonsOnStop();
        _eventBus.post(ControlEventFactory.createEventOfType(STOP));
    }

    private void startAction() {
        _debugeeInput.set("");
        _processConsole.set("");
        _startActionCommand.canExecute().set(false);
        _stopActionCommand.canExecute().set(true);
        _stepOverActionCommand.canExecute().set(true);
        _stepIntoActionCommand.canExecute().set(true);
        _stepOutActionCommand.canExecute().set(true);
        _pauseActionCommand.canExecute().set(true);
        _resumeActionCommand.canExecute().set(true);

        ControlEventHandler controlEventHandler = _processFactory.buildComponents(
                new StartDefinition(_javaPath.get(), _className.get(), _classPath.get(), _arguments.get()));

        ExecutorService service = ThreadBuilder.createService("control-event-handler");
        service.submit(() -> {
            try {
                controlEventHandler.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

    //region Properties
    public StringProperty getArgumentsProperty() {
        return _arguments;
    }

    public StringProperty getJavaPathProperty() {
        return _javaPath;
    }

    public StringProperty getClassPathProperty() {
        return _classPath;
    }

    public StringProperty getClassNameProperty() {
        return _className;
    }

    public StringProperty getProcessConsoleProperty() {
        return _processConsole;
    }

    public StringProperty getDebuggerConsoleProperty() {
        return _debuggerConsole;
    }

    public ICommand getPauseActionCommand() {
        return _pauseActionCommand;
    }

    public ICommand getResumeActionCommand() {
        return _resumeActionCommand;
    }

    public ICommand getStepOverActionCommand() {
        return _stepOverActionCommand;
    }

    public ICommand getStartActionCommand() {
        return _startActionCommand;
    }

    public ICommand getStopActionCommand() {
        return _stopActionCommand;
    }

    public ICommand getStepIntoActionCommand() {
        return _stepIntoActionCommand;
    }

    public ICommand getStepOutActionCommand() {
        return _stepOutActionCommand;
    }
    //endregion
}
