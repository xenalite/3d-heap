package com.heap3d.interfaces.viewmodels;

import com.google.common.eventbus.Subscribe;
import com.heap3d.implementations.application.ControlEventHandler;
import com.heap3d.implementations.events.ControlEventFactory;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.StartDefinition;
import com.heap3d.implementations.factories.ThreadBuilder;
import com.heap3d.utilities.ICommand;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ExecutorService;

import static com.heap3d.implementations.events.EventType.STOP;

/**
 * Created by om612 on 04/12/14.
 */
public interface IApplicationTabViewModel {

    public void handleProcessEvent(ProcessEvent pe);

    public StringProperty getArgumentsProperty();
    public StringProperty getJavaPathProperty();
    public StringProperty getClassPathProperty();
    public StringProperty getClassNameProperty();
    public StringProperty getProcessConsoleProperty();
    public StringProperty getDebuggerConsoleProperty();

    public ICommand getPauseActionCommand();
    public ICommand getResumeActionCommand();
    public ICommand getStepOverActionCommand();
    public ICommand getStartActionCommand();
    public ICommand getStopActionCommand();
    public ICommand getStepIntoActionCommand();
    public ICommand getStepOutActionCommand();
}
