package com.heap3d.application.utilities;

import javafx.beans.property.BooleanProperty;

/**
 * Created by oskar on 08/11/14.
 */
public interface ICommand {

    public void execute();

    public BooleanProperty canExecute();
}
