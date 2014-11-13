package com.imperial.heap3d.utilities;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.application.ControlEventHandler;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.controllers.BreakpointsController;
import com.imperial.heap3d.ui.controllers.MainWindowController;
import com.imperial.heap3d.ui.viewmodels.BreakpointsViewModel;
import com.imperial.heap3d.ui.viewmodels.MainWindowViewModel;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.OptInCaching;

/**
 * Created by oskar on 31/10/14.
 */
public class TypeRegistry {

    private final MutablePicoContainer _injector;

    public TypeRegistry() {
        _injector = new DefaultPicoContainer(new OptInCaching());

        _injector.as(Characteristics.CACHE).addComponent(VirtualMachineProvider.class);
        _injector.as(Characteristics.CACHE).addComponent(EventBus.class);
        _injector.as(Characteristics.CACHE).addComponent(ControlEventHandler.class);

        _injector.addComponent(MainWindowController.class);
        _injector.addComponent(BreakpointsController.class);

        _injector.addComponent(BreakpointsViewModel.class);
        _injector.addComponent(MainWindowViewModel.class);
    }

    public PicoContainer getInjector() {
        return _injector;
    }
}
