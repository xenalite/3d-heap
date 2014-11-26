package com.imperial.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.application.ControlEventHandler;
import com.imperial.heap3d.application.DebuggedProcess;
import com.imperial.heap3d.events.ControlEventFactory;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.utilities.ICommand;
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
public class ApplicationTabViewModel {
    private EventBus _eventBus;
    private IVirtualMachineProvider _VMProvider;
    private HeapGraphFactory _heapGraphFactory;

    private ICommand _resumeActionCommand;
    private ICommand _pauseActionCommand;
    private ICommand _stepOverActionCommand;
    private ICommand _stepIntoActionCommand;
    private ICommand _stepOutActionCommand;
    private ICommand _startActionCommand;
    private ICommand _stopActionCommand;
    private ICommand _screenShotCommand;
    private StringProperty _javaPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private StringProperty _screenShotPath;
    private StringProperty _processConsole;
    private StringProperty _debugeeInput;
    private StringProperty _arguments;
    private StringProperty _debuggerConsole;

    public ApplicationTabViewModel(EventBus eventBus, IVirtualMachineProvider VMProvider, HeapGraphFactory heapGraphFactory) {
        _VMProvider = VMProvider;
        _eventBus = eventBus;
        _eventBus.register(this);
        _heapGraphFactory = heapGraphFactory;

        _startActionCommand = new RelayCommand(this::startAction);
        _startActionCommand.canExecute().set(true);
        _stopActionCommand = new RelayCommand(this::stopAction);
        _pauseActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(PAUSE)));
        _resumeActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(RESUME)));
        _stepOverActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPOVER)));
        _stepIntoActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPINTO)));
        _stepOutActionCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createEventOfType(STEPOUT)));
        _screenShotPath = new SimpleStringProperty(this, "", "ScreenShot/img");
        _className = new SimpleStringProperty(this, "", "test_programs.linked_list_null.Program");
        _screenShotCommand = new RelayCommand(() -> _eventBus.post(ControlEventFactory.createScreenShotEvent(_screenShotPath.getValue())));
        _className = new SimpleStringProperty(this, "", "tests.system.testprograms.linked_list_null.Program");
        _classPath = new SimpleStringProperty(this, "", System.getProperty("user.home") + "/workspace/3d-heap/Debugger/out/production/Debugger/");
        _javaPath = new SimpleStringProperty(this, "", System.getProperty("java.home") + "/bin/java");
        _processConsole = new SimpleStringProperty("this", "", "");
        _debugeeInput = new SimpleStringProperty("this", "", "");
        _arguments = new SimpleStringProperty(this, "", "");
        _debuggerConsole = new SimpleStringProperty(this, "","");
    }

    @Subscribe
    public void handleProcessEvent(ProcessEvent pe) {
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
                    Platform.runLater(() -> _debuggerConsole.set(pe.message));
                }
                break;
                case PROCESS_MSG: {
                    Platform.runLater(() -> _processConsole.set(_processConsole.get()
                            + System.lineSeparator() + pe.message));
                }
            }
        }
        catch(IllegalStateException e) { System.out.println(e); }
    }

    private void setButtonsOnStop() {
        _startActionCommand.canExecute().set(true);
        _stopActionCommand.canExecute().set(false);
        _stepOverActionCommand.canExecute().set(false);
        _stepIntoActionCommand.canExecute().set(false);
        _stepOutActionCommand.canExecute().set(false);
        _pauseActionCommand.canExecute().set(false);
        _resumeActionCommand.canExecute().set(false);
        _screenShotCommand.canExecute().set(false);
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
        _screenShotCommand.canExecute().set(true);

        StartDefinition sd = new StartDefinition(_javaPath.get(), _className.get(), _classPath.get());
        DebuggedProcess dprocess = new DebuggedProcess(sd, _VMProvider, _eventBus, _heapGraphFactory);
        ControlEventHandler handler = new ControlEventHandler(dprocess, _eventBus);
        _eventBus.post(ControlEventFactory.createEventOfType(START));

        ExecutorService service = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

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
    public StringProperty getArgumentsProperty() {
        return _arguments;
    }

    public StringProperty getJavaPathProperty() { return _javaPath; }

    public StringProperty getClassPathProperty() {
        return _classPath;
    }

    public StringProperty getClassNameProperty() {
        return _className;
    }
    public StringProperty getScreenShotPath() {
        return _screenShotPath;
    }
    public StringProperty getProcessConsoleProperty() { return _processConsole; }

    public StringProperty getDebuggerConsoleProperty() {
        return _debuggerConsole;
    }

    public ICommand getPauseActionCommand() { return _pauseActionCommand; }

    public ICommand getResumeActionCommand() { return _resumeActionCommand; }

    public ICommand getStepOverActionCommand() { return _stepOverActionCommand; }

    public ICommand getStartActionCommand() { return _startActionCommand; }

    public ICommand getStopActionCommand() { return _stopActionCommand; }

    public ICommand getStepIntoActionCommand() {
        return _stepIntoActionCommand;
    }

    public ICommand getStepOutActionCommand() {
        return _stepOutActionCommand;
    }

    public ICommand getScreenShotActionCommand() { return _screenShotCommand; }
    //endregion
}
