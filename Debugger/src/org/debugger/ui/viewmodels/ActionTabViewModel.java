package org.debugger.ui.viewmodels;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.debugger.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.VirtualMachine;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

/**
 * Created by oskar on 29/10/14.
 */
public class ActionTabViewModel {

    private IVirtualMachineProvider _virtualMachineProvider;
    private EventBus _eventBus;
    private StringProperty _breakpoint;
    private Property<ObservableList<String>> _breakpoints;

    public ActionTabViewModel(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _eventBus.register(this);
        _breakpoint = new SimpleStringProperty(this, "breakpoint", "");
        _breakpoints = new SimpleObjectProperty<ObservableList<String>>(this, "breakpoints", FXCollections.observableList(new ArrayList<String>()));
    }

    @Subscribe
    public void handle(ChangeEvent e) {
        System.out.println(e);
    }

    public void pauseAction() {
        VirtualMachine vm = _virtualMachineProvider.getVirtualMachine();
        if(vm != null) {
            vm.suspend();
        }
    }

    public void resumeAction() {
        VirtualMachine vm = _virtualMachineProvider.getVirtualMachine();
        if(vm != null) {
            vm.resume();
        }
    }

    public void stepAction() {

    }

    public void addAction() {
        String currentBreakpoint = _breakpoint.getValue();
        if(isValidBreakpoint(currentBreakpoint)) {
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
