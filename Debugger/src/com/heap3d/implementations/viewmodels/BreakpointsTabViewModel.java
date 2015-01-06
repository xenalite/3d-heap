package com.heap3d.implementations.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.implementations.events.*;
import com.heap3d.interfaces.viewmodels.IBreakpointsTabViewModel;
import com.heap3d.utilities.Check;
import com.heap3d.utilities.PathUtils;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.heap3d.implementations.events.EventType.BREAKPOINT;
import static com.heap3d.implementations.events.EventType.WATCHPOINT;

/**
 * Created by oskar on 29/10/14.
 */
public class BreakpointsTabViewModel implements IBreakpointsTabViewModel {

    private final String DELIM = ":";
    private EventBus _eventBus;
    private Property<String> _breakpointClass = new SimpleStringProperty();
    private Property<String> _breakpointMethod = new SimpleStringProperty();
    private Property<String> _watchpointClass = new SimpleStringProperty();
    private Property<String> _watchpointField = new SimpleStringProperty();

    private List<ControlEvent> _cachedElements;
    private Property<ObservableList<String>> _breakpoints = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private Property<ObservableList<String>> _watches = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private boolean _cacheEnabled;

    public BreakpointsTabViewModel(EventBus eventBus) {
        _eventBus = Check.notNull(eventBus, "eventBus");
        _eventBus.register(this);
        _cacheEnabled = true;
        _cachedElements = new ArrayList<>();

        // ---- This is for convenience only. ----
        _breakpointClass.setValue(PathUtils.TEST_PROGRAM_CLASS_NAME);
        _breakpointMethod.setValue(PathUtils.TEST_PROGRAM_BREAKPOINT_METHOD_NAME);
        addBreakpointAction();
    }

    @Override
    @Subscribe
    public void handleProcessEvent(ProcessEvent processEvent) {
        if(processEvent.type == ProcessEventType.STARTED) {
            _cacheEnabled = false;
            send();
        }
        if(processEvent.type == ProcessEventType.STOPPED) {
            _cacheEnabled = true;
            cache();
        }
    }

    private synchronized void cache() {
        _cachedElements.clear();
        addToCache(_breakpoints.getValue(), BREAKPOINT);
        addToCache(_watches.getValue(), WATCHPOINT);
    }

    private void addToCache(List<String> list, EventType type) {
        for(String contents : list) {
            String[] values = contents.split(DELIM);
            _cachedElements.add(createEvent(type, values[0], values[1]));
        }
    }

    @Override
    public void addBreakpointAction() {
        addElement(_breakpointClass, _breakpointMethod, _breakpoints.getValue(), BREAKPOINT);
    }

    @Override
    public void addWatchpointAction() {
        addElement(_watchpointClass, _watchpointField, _watches.getValue(), WATCHPOINT);
    }

    private void addElement(Property<String> classNameProperty, Property<String> pointProperty,
                            List<String> list, EventType type) {
        String className = classNameProperty.getValue();
        String point = pointProperty.getValue();
        if(isValid(className, point)) {
            list.add(className + DELIM + point);
            classNameProperty.setValue("");
            pointProperty.setValue("");
            _cachedElements.add(createEvent(type, className, point));
            send();
        }

    }

    private synchronized void send() {
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

    @Override
    public Property<String> getBreakpointClassProperty() {
        return _breakpointClass;
    }
    @Override
    public Property<String> getBreakpointMethodProperty() { return _breakpointMethod; }
    @Override
    public Property<ObservableList<String>> getBreakpointsProperty() {
        return _breakpoints;
    }

    @Override
    public Property<String> getWatchpointClassProperty() { return _watchpointClass; }
    @Override
    public Property<String> getWatchpointFieldProperty() { return _watchpointField; }
    @Override
    public Property<ObservableList<String>> getWatchpointsProperty() { return _watches; }

    @Override
    public void removeBreakpointAction(String selectedItem) {
        String[] values = selectedItem.split(DELIM);
        if(values.length != 2)
            return;
        _breakpoints.getValue().remove(selectedItem);
        for (ControlEvent ce : _cachedElements) {
            if (Objects.equals(ce.className, values[0]) && Objects.equals(ce.argument, values[1]) &&
                    ce.type == BREAKPOINT) {

                _cachedElements.remove(ce);
                return;
            }
        }
        _eventBus.post(ControlEventFactory.createRemoveBreakpointEvent(values[0],values[1]));
    }

    @Override
    public void removeWatchpointAction(String selectedItem) {
        String[] values = selectedItem.split(DELIM);
        if(values.length != 2)
            return;
        _watches.getValue().remove(selectedItem);
        for(ControlEvent ce : _cachedElements) {
            if (Objects.equals(ce.className, values[0]) && Objects.equals(ce.argument, values[1]) &&
                    ce.type == WATCHPOINT) {

                _cachedElements.remove(ce);
                return;
            }
        }
        _eventBus.post(ControlEventFactory.createRemoveWatchpointEvent(values[0], values[1]));
    }
}