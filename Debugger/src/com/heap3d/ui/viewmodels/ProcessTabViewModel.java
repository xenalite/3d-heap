package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventUtils;
import com.heap3d.application.EventHandler;
import com.heap3d.application.utilities.EventHandlerFactory;
import javafx.application.Platform;
import javafx.beans.property.*;

import javax.swing.event.ChangeEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oskar on 29/10/14.
 */
public class ProcessTabViewModel {

    private static final int DEFAULT_PORT = 8000;

    private EventHandler _currentHandler;
    private EventHandlerFactory _eventHandlerFactory;
    private EventBus _eventBus;
    private StringProperty _status;
    private StringProperty _jdkPath;
    private StringProperty _classPath;
    private StringProperty _className;
    private IntegerProperty _port;
    private StringProperty _sourceCode;
    private BooleanProperty _disableStart;

    public ProcessTabViewModel(EventBus eventBus, EventHandlerFactory eventHandlerFactory) {
        _eventHandlerFactory = eventHandlerFactory;
        _eventBus = eventBus;
        _eventBus.register(this);
        _className = new SimpleStringProperty(this, "className", "Debugee");
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

    @Subscribe
    public void handleChangeEvent(ChangeEvent e) {
        Platform.runLater(() -> _sourceCode.set(e.getSource().toString()));
    }

    public void stopAction() {
        _status.set("NOT RUNNING");
        _disableStart.set(false);
//        _eventBus.post(EventUtils.createNewDestroyEvent());
    }

    public void startAction() {
        _status.set("RUNNING");

        _disableStart.set(true);
        _currentHandler = _eventHandlerFactory.create();

//        _eventBus.post(EventUtils.createNewStartEvent(new StartDefinition(
//                _jdkPath.get(), _classPath.get(), _className.get(), _port.get()
//        )));

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            try {
                _currentHandler.run();
            }
            catch(InterruptedException ignored) {}
        });
        service.shutdown();
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