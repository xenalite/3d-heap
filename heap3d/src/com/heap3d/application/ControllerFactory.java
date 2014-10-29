package com.heap3d.application;

import javafx.util.Callback;
import org.picocontainer.PicoContainer;

/**
 * Created by oskar on 29/10/14.
 */
public class ControllerFactory implements Callback<Class<?>, Object> {

    private final PicoContainer _injector;

    public ControllerFactory(PicoContainer injector) {
        _injector = injector;
    }

    @Override
    public Object call(Class<?> param) {
        return _injector.getComponent(param);
    }
}
