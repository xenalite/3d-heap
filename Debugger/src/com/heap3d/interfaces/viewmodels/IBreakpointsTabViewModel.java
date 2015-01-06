package com.heap3d.interfaces.viewmodels;

import com.heap3d.implementations.events.ProcessEvent;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;

/**
 * Created by oskar on 06/01/15.
 */
public interface IBreakpointsTabViewModel {

    public void handleProcessEvent(ProcessEvent processEvent);

    public void addBreakpointAction();

    public void addWatchpointAction();

    public Property<String> getBreakpointClassProperty();

    public Property<String> getBreakpointMethodProperty();

    public Property<ObservableList<String>> getBreakpointsProperty();

    public Property<String> getWatchpointClassProperty();

    public Property<String> getWatchpointFieldProperty();

    public Property<ObservableList<String>> getWatchpointsProperty();

    public void removeBreakpointAction(String selectedItem);

    public void removeWatchpointAction(String selectedItem);
}
