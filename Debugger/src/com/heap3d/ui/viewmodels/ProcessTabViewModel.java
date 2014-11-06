package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.EventHandler;
import com.heap3d.application.events.EventUtils;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.EventHandlerFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.heap3d.application.events.EventType.*;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabViewModel {

    private EventHandlerFactory _eventHandlerFactory;
    private EventBus _eventBus;
    private StringProperty _status;
    private StringProperty _javaPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private SimpleStringProperty _debuggerOutput;
    private StringProperty _jvmArgs;
    private BooleanProperty _enableButtons;

    public ProcessTabViewModel(EventBus eventBus, EventHandlerFactory eventHandlerFactory) {
        _eventHandlerFactory = eventHandlerFactory;
        _eventBus = eventBus;
        _eventBus.register(this);
        _className = new SimpleStringProperty(this, "className", "com.heap3d.application.Debugee");
        _classPath = new SimpleStringProperty(this, "classpath", "~/workspace/3d-heap/Debugger/out/production/Debugger/");
        _javaPath = new SimpleStringProperty(this, "jdkPath", System.getProperty("java.home") + "/bin/java");
        _status = new SimpleStringProperty(this, "status", "NOT RUNNING");
        _debuggerOutput = new SimpleStringProperty("this", "debuggerOutput", "");
        _jvmArgs = new SimpleStringProperty(this, "jvmArgs", "");
        _enableButtons = new SimpleBooleanProperty(this, "disableStart", true);
    }

    public void stopAction() {
        _status.set("STOPPED");
        _enableButtons.set(true);
        _eventBus.post(EventUtils.createControlEvent(STOP));
    }

    public void startAction() {
        _status.set("RUNNING");
        _enableButtons.set(false);

        int port = getRandomPort();
        StartDefinition sd = new StartDefinition(_className.get(), buildCommand(port), port);
        EventHandler handler = _eventHandlerFactory.create(sd);
        _eventBus.post(EventUtils.createControlEvent(START));

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

    private String buildCommand(int port) {
        StringBuilder sb = new StringBuilder();
        sb.append(_javaPath.get());
        sb.append(" ");
        sb.append("-agentlib:jdwp=transport=dt_socket,address=");
        sb.append(port);
        sb.append(",server=n,suspend=y");
        sb.append(" -cp ");
        sb.append(_classPath.get());
        sb.append(" ");

        return sb.toString();
    }

    private void appendToOutput(Object value) {
        _debuggerOutput.set(_debuggerOutput.get() + System.lineSeparator() + value);
    }

    public BooleanProperty getDisableButtons() {
        return _enableButtons;
    }

    public StringProperty getJvmArgs() {
        return _jvmArgs;
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

    public void pauseAction() {
        _eventBus.post(EventUtils.createControlEvent(PAUSE));
    }

    public void resumeAction() {
        _eventBus.post(EventUtils.createControlEvent(RESUME));
    }

    public void stepAction() {
    }

    public int getRandomPort() {
        final int RANGE = 2000;
        final int MINIMUM = 8000;
//        return 10000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
    }
}