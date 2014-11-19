package com.imperial.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.events.*;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.imperial.heap3d.events.EventType.BREAKPOINT;
import static com.imperial.heap3d.events.EventType.WATCHPOINT;

/**
 * Created by oskar on 29/10/14.
 */
public class BreakpointsViewModel {

    private final String DELIM = ":";
    private EventBus _eventBus;
    private StringProperty _breakpointClass;
    private StringProperty _breakpointMethod;
    private StringProperty _watchpointClass;
    private StringProperty _watchpointField;

    private Vector<ControlEvent> _cachedElements;
    private Property<ObservableList<String>> _breakpoints;
    private Property<ObservableList<String>> _watchpoints;
    private boolean _cacheEnabled;

    public BreakpointsViewModel(EventBus eventBus) {
        if(eventBus == null)
            throw new IllegalArgumentException("eventBus");
        _eventBus = eventBus;
        _eventBus.register(this);
        _cacheEnabled = true;
        _breakpointClass = new SimpleStringProperty(this, "", "");
        _breakpointMethod = new SimpleStringProperty(this, "", "");
        _watchpointClass = new SimpleStringProperty(this, "", "");
        _watchpointField = new SimpleStringProperty(this, "", "");
        _breakpoints = new SimpleObjectProperty<>(this, "", FXCollections.observableList(new ArrayList<>()));
        _watchpoints = new SimpleObjectProperty<>(this, "", FXCollections.observableList(new ArrayList<>()));
        _cachedElements = new Vector<>();

        // TODO: REMOVE AFTER TESTING -- THIS IS FOR CONVENIENCE ONLY
        _breakpointClass.set("test.Program");
        _breakpointMethod.set("main");
//        addElement(_breakpointClass, _breakpointMethod, _breakpoints.getValue(), BREAKPOINT);

        _breakpointClass.set("test.Program");
        _breakpointMethod.set("method");
//        addElement(_breakpointClass, _breakpointMethod, _breakpoints.getValue(), BREAKPOINT);

        _breakpointClass.set("test.Program");
        _breakpointMethod.set("append");
//        addElement(_breakpointClass, _breakpointMethod, _breakpoints.getValue(), BREAKPOINT);
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
    public void handleProcessEvent(ProcessEvent pe) {
        if(pe.type == ProcessEventType.STOPPED) {
            _cacheEnabled = true;
            cache();
        }
    }

    private synchronized void cache() {
        _cachedElements.clear();
        addToCache(_breakpoints.getValue(), BREAKPOINT);
        addToCache(_watchpoints.getValue(), WATCHPOINT);
    }

    private void addToCache(List<String> list, EventType type) {
        for(String contents : list) {
            String[] values = contents.split(DELIM);
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
            list.add(className + DELIM + point);
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

    public void removeBreakpointAction(String selectedItem) {
        String[] values = selectedItem.split(DELIM);
        _breakpoints.getValue().remove(selectedItem);
        _cachedElements = _cachedElements.stream().filter(ce -> Objects.equals(ce.className, values[0]) &&
                Objects.equals(ce.argument, values[1]) &&
                ce.type == BREAKPOINT).collect(Collectors.toCollection(Vector::new));
    }

    public void removeWatchpointAction(String selectedItem) {
        String[] values = selectedItem.split(DELIM);
        _watchpoints.getValue().remove(selectedItem);
        _cachedElements = _cachedElements.stream().filter(ce -> Objects.equals(ce.className, values[0]) &&
                Objects.equals(ce.argument, values[1]) &&
                ce.type == WATCHPOINT).collect(Collectors.toCollection(Vector::new));
    }
}