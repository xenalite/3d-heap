package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.ControlEvent;
import com.heap3d.application.events.ProcessEvent;
import com.heap3d.application.events.ProcessEventType;
import com.heap3d.application.events.StartDefinition;
import com.heap3d.application.utilities.DebuggedProcess;
import com.heap3d.application.utilities.IVirtualMachineProvider;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private DebuggedProcess _dprocess;
    private EventBus _eventBus;
    private ConcurrentLinkedDeque<ControlEvent> _controlEventQueue;

    public EventHandler(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _dprocess = new DebuggedProcess(definition, provider, eventBus);
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        _controlEventQueue.add(e);
    }

    public void loop() throws InterruptedException, IOException {
        while (true) {
            while (!_controlEventQueue.isEmpty())
                if(!handleControlQueueItem(_controlEventQueue.removeFirst()))
                    return;

            if(!_dprocess.waitForEvents())
                return;
        }
    }

    private boolean handleControlQueueItem(ControlEvent e) throws InterruptedException {
        switch (e.type) {
            case START: {
                _dprocess.start();
            }
            break;
            case PAUSE: {
                _dprocess.pause();
            }
            break;
            case RESUME: {
                _dprocess.resume();
            }
            break;
            case STEP: {
                _dprocess.createStepRequest();
            }
            break;
            case BREAKPOINT: {
                _dprocess.cacheBreakpointUntilClassIsLoaded(e.className, e.argument);
            }
            break;
            case WATCHPOINT: {
                _dprocess.cacheWatchpointUntilClassIsLoaded(e.className, e.argument);
            }
            break;
            case STOP: {
                return false;
            }
        }
        return true;
    }

    public void run() throws InterruptedException, IOException {
        loop();
        dispose();
    }

    private void dispose() {
        _dprocess.dispose();

        _eventBus.post(new ProcessEvent(ProcessEventType.STOPPED));
        _eventBus.unregister(this);
    }
}