package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.ControlEvent;
import com.heap3d.application.events.ControlEventFactory;
import com.heap3d.application.events.EventType;
import com.heap3d.application.utilities.ProcessState;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.heap3d.application.events.EventType.BREAKPOINT;
import static com.heap3d.application.events.EventType.WATCHPOINT;
import static com.heap3d.application.utilities.ProcessState.STOPPED;

/**
 * Created by oskar on 29/10/14.
 */
public class BreakpointsTabViewModel {

    private EventBus _eventBus;
    private StringProperty _breakpointClass;
    private StringProperty _breakpointMethod;
    private StringProperty _watchpointClass;
    private StringProperty _watchpointField;

    private Vector<ControlEvent> _cachedElements;
    private Property<ObservableList<String>> _breakpoints;
    private Property<ObservableList<String>> _watchpoints;
    private boolean _cacheEnabled;

    public BreakpointsTabViewModel(EventBus eventBus) {
        _eventBus = eventBus;
        _eventBus.register(this);
        _cacheEnabled = true;
        _breakpointClass = new SimpleStringProperty(this, "breakpointClass", "");
        _breakpointMethod = new SimpleStringProperty(this, "breakpointMethod", "");
        _watchpointClass = new SimpleStringProperty(this, "watchpointClass", "");
        _watchpointField = new SimpleStringProperty(this, "watchpointField", "");
        _breakpoints = new SimpleObjectProperty<>(this, "breakpoints", FXCollections.observableList(new ArrayList<>()));
        _watchpoints = new SimpleObjectProperty<>(this, "watchpoints", FXCollections.observableList(new ArrayList<>()));
        _cachedElements = new Vector<>();
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        if(e.type == EventType.START) {
            _cacheEnabled = false;
            send();
        }
        else if(e.type == EventType.STOP) {
            _cacheEnabled = true;
            cache();
        }
    }

    @Subscribe
    public void handleEvent2(ProcessState s) {
        if(s == STOPPED) {
            _cacheEnabled = true;
            cache();
        }
    }

    private synchronized void cache() {
        addToCache(_breakpoints.getValue(), BREAKPOINT);
        addToCache(_watchpoints.getValue(), WATCHPOINT);
    }

    private void addToCache(List<String> list, EventType type) {
        _cachedElements.clear();
        for(String contents : list) {
            String[] values = contents.split(":");
            _cachedElements.add(createEvent(type, values[0], values[1]));
        }
    }

    public void addBreakpointAction() {
        addElement(_breakpointClass, _breakpointMethod, _breakpoints.getValue(), BREAKPOINT);
    }

    public void addWatchpointAction() {
        addElement(_watchpointClass, _watchpointField, _watchpoints.getValue(), WATCHPOINT);
    }

    private void addElement(StringProperty classNameProperty, StringProperty pointProperty,
                            List<String> list, EventType type) {
        String className = classNameProperty.get();
        String point = pointProperty.get();
        if(isValid(className, point)) {
            list.add(className + ":" + point);
            classNameProperty.set("");
            pointProperty.set("");
            _cachedElements.add(createEvent(type, className, point));
            send();
        }
    }

    private void send() {
        if(!_cacheEnabled) {
            _cachedElements.forEach(_eventBus::post);
            _cachedElements.clear();
        }
    }

    private ControlEvent createEvent(EventType type, String className, String point) {
        return (type == BREAKPOINT) ?
                ControlEventFactory.createBreakpointEvent(className, point) :
                ControlEventFactory.createWatchpointEvent(className, point);
    }

    private boolean isValid(String className, String point) {
        return !className.isEmpty() && !point.isEmpty();
    }

    public StringProperty getBreakpointClassProperty() {
        return _breakpointClass;
    }
    public StringProperty getBreakpointMethodProperty() { return _breakpointMethod; }
    public Property<ObservableList<String>> getBreakpointsProperty() {
        return _breakpoints;
    }

    public StringProperty getWatchpointClassProperty() { return _watchpointClass; }
    public StringProperty getWatchpointFieldProperty() { return _watchpointField; }
    public Property<ObservableList<String>> getWatchpointsProperty() { return _watchpoints; }
}