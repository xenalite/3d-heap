package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.EventHandler;
import com.heap3d.ui.controllers.MainWindowController;
import com.heap3d.ui.controllers.ProcessTabController;
import com.heap3d.ui.viewmodels.ActionTabViewModel;
import com.heap3d.ui.controllers.ActionsTabController;
import com.heap3d.ui.viewmodels.EventHandlerFactory;
import com.heap3d.ui.viewmodels.MainWindowViewModel;
import com.heap3d.ui.viewmodels.ProcessTabViewModel;
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

        _injector.as(Characteristics.CACHE).addComponent(EventHandlerFactory.class);
        _injector.as(Characteristics.CACHE).addComponent(VirtualMachineProvider.class);
        _injector.as(Characteristics.CACHE).addComponent(EventBus.class);
        _injector.as(Characteristics.CACHE).addComponent(EventHandler.class);

        _injector.addComponent(MainWindowController.class);
        _injector.addComponent(ActionsTabController.class);
        _injector.addComponent(ProcessTabController.class);

        _injector.addComponent(MainWindowViewModel.class);
        _injector.addComponent(ActionTabViewModel.class);
        _injector.addComponent(ProcessTabViewModel.class);
    }

    public PicoContainer getInjector() {
        return _injector;
    }
}
