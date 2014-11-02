package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.EventUtils;
import com.heap3d.application.events.IEvent;
import com.heap3d.application.events.definitions.BreakpointDefinition;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by oskar on 29/10/14.
 */
public class ActionTabViewModel {

    private EventBus _eventBus;
    private StringProperty _breakpoint;
    private Property<ObservableList<String>> _breakpoints;

    public ActionTabViewModel(EventBus eventBus) {
        _eventBus = eventBus;
        _eventBus.register(this);
        _breakpoint = new SimpleStringProperty(this, "breakpoint", "");
        _breakpoints = new SimpleObjectProperty<>(this, "breakpoints", FXCollections.observableList(new ArrayList<>()));
    }

    public void pauseAction() {
        _eventBus.post(EventUtils.createNewPauseEvent());
    }

    public void resumeAction() {
        _eventBus.post(EventUtils.createNewResumeEvent());
    }

    public void stepAction() {
    }

    public void addAction() {
        String currentBreakpoint = _breakpoint.getValue();
        if(isValidBreakpoint(currentBreakpoint)) {
            String[] split = currentBreakpoint.split(":");
            BreakpointDefinition bd = new BreakpointDefinition(split[1], split[2], Integer.parseInt(split[3]));
            IEvent<BreakpointDefinition> nbe = EventUtils.createNewBreakpointEvent(bd);
            _eventBus.post(nbe);
            _breakpoints.getValue().add(currentBreakpoint);
            _breakpoint.set("");
        }
    }

    private boolean isValidBreakpoint(String candidate) {
        return !candidate.isEmpty();
    }

    public StringProperty getBreakpointProperty() {
        return _breakpoint;
    }

    public Property<ObservableList<String>> getBreakpointsProperty() {
        return _breakpoints;
    }
}
