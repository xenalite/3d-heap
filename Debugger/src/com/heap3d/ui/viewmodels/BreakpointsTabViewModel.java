package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.Event;
import com.heap3d.application.events.EventType;
import com.heap3d.application.events.EventUtils;
import com.heap3d.application.utilities.ProcessState;
import com.sun.xml.internal.fastinfoset.stax.events.EventBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by oskar on 29/10/14.
 */
public class BreakpointsTabViewModel {

    private EventBus _eventBus;
    private StringProperty _breakpoint;
    private StringProperty _watchpoint;

    private Vector<Event> _cachedBreakpoints;
    private Vector<Event> _cachedWatchpoints;

    private Property<ObservableList<String>> _breakpoints;
    private Property<ObservableList<String>> _watchpoints;
    private ProcessState _state;

    public BreakpointsTabViewModel(EventBus eventBus) {
        _eventBus = eventBus;
        _eventBus.register(this);
        _state = ProcessState.STOPPED;
        _breakpoint = new SimpleStringProperty(this, "breakpoint", "");
        _watchpoint = new SimpleStringProperty(this, "watchpoint", "");
        _breakpoints = new SimpleObjectProperty<>(this, "breakpoints", FXCollections.observableList(new ArrayList<>()));
        _watchpoints = new SimpleObjectProperty<>(this, "watchpoints", FXCollections.observableList(new ArrayList<>()));
        _cachedBreakpoints = new Vector<>();
        _cachedWatchpoints = new Vector<>();
    }

    @Subscribe
    public void handleEvent(Event e) {
        if(e.type == EventType.START) {
            _state = ProcessState.RUNNING;
            send();
        }
        else if(e.type == EventType.STOP) {
            _state = ProcessState.STOPPED;
        }
    }

    public void addBreakpointAction() {
        String candidate = _breakpoint.get();
        if(isValid(candidate)) {
            _breakpoints.getValue().add(candidate);
            _breakpoint.set("");
            _cachedBreakpoints.add(createEvent(EventType.BREAKPOINT, candidate));
            send();
        }
    }

    public void addWatchpointAction() {
        String candidate = _watchpoint.get();
        if(isValid(candidate)) {
            _watchpoints.getValue().add(candidate);
            _watchpoint.set("");
            _cachedWatchpoints.add(createEvent(EventType.WATCHPOINT, candidate));
            send();
        }
    }

    private void send() {
        if(_state == ProcessState.RUNNING) {
            _cachedBreakpoints.forEach(_eventBus::post);
            _cachedWatchpoints.forEach(_eventBus::post);
            _cachedBreakpoints.clear();
            _cachedWatchpoints.clear();
        }
    }

    private Event createEvent(EventType type, String candidate) {
        String[] values = candidate.split(":");

        return (type == EventType.BREAKPOINT) ?
                EventUtils.createBreakpointEvent(values[0], values[1]) :
                EventUtils.createWatchpointEvent(values[0], values[1]);
    }

    private boolean isValid(String candidate) {
        return !candidate.isEmpty();
    }

    public StringProperty getBreakpointProperty() {
        return _breakpoint;
    }
    public Property<ObservableList<String>> getBreakpointsProperty() {
        return _breakpoints;
    }

    public StringProperty getWatchpointProperty() { return _watchpoint; }
    public Property<ObservableList<String>> getWatchpointsProperty() { return _watchpoints; }
}