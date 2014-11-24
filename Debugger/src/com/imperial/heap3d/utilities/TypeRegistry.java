package com.imperial.heap3d.utilities;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.VirtualMachineProvider;
import com.imperial.heap3d.ui.controllers.ApplicationTabController;
import com.imperial.heap3d.ui.controllers.BottomPanelController;
import com.imperial.heap3d.ui.controllers.BreakpointsTabController;
import com.imperial.heap3d.ui.viewmodels.ApplicationTabViewModel;
import com.imperial.heap3d.ui.viewmodels.BreakpointsTabViewModel;
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
        _injector.as(Characteristics.CACHE).addComponent(HeapGraphFactory.class);

        _injector.addComponent(ApplicationTabController.class);
        _injector.addComponent(BreakpointsTabController.class);
        _injector.addComponent(BottomPanelController.class);

        _injector.as(Characteristics.CACHE).addComponent(ApplicationTabViewModel.class);
        _injector.addComponent(BreakpointsTabViewModel.class);
    }

    public PicoContainer getInjector() {
        return _injector;
    }
}
