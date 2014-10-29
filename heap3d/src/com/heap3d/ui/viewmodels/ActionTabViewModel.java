package com.heap3d.ui.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Created by oskar on 29/10/14.
 */
public class ActionTabViewModel {
    private StringProperty _breakpoint;
    private ObservableList<String> _breakpoints;

    public ActionTabViewModel() {
        _breakpoint = new SimpleStringProperty(this, "breakpoint", "");

    }

    public void pauseAction() {

    }

    public void resumeAction() {

    }

    public void stepAction() {

    }

    public void addAction() {

    }

    public StringProperty getBreakpointProperty() {
        return _breakpoint;
    }
}
