package com.heap3d.ui.viewmodels;

import com.google.common.eventbus.EventBus;

/**
 * Created by oskar on 29/10/14.
 */
public class MainWindowViewModel {

    private EventBus _eventBus;

    public MainWindowViewModel(EventBus eventBus) {
        _eventBus = eventBus;
    }
}
