package com.heap3d.utilities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Created by oskar on 08/11/14.
 */
public class RelayCommand implements ICommand {

    private BooleanProperty _enabled;
    private Runnable _task;

    public RelayCommand(Runnable task) {
        if(task == null)
            throw new IllegalArgumentException("task");
        _task = task;
        _enabled = new SimpleBooleanProperty(this, "enabled", false);
    }

    @Override
    public void execute() {
        if(_enabled.get()) {
            _task.run();
        }
    }

    @Override
    public BooleanProperty canExecute() {
        return _enabled;
    }
}
