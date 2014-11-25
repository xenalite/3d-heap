package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.events.ControlEvent;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.ProcessEventType;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

/**
 * Created by oskar on 31/10/14.
 */
public class ControlEventHandler {

    private DebuggedProcess _dprocess;
    private EventBus _eventBus;
    private ConcurrentLinkedDeque<ControlEvent> _controlEventQueue;
    private Semaphore _semaphore;

    public ControlEventHandler(DebuggedProcess dprocess, EventBus eventBus) {
        _dprocess = dprocess;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _semaphore = new Semaphore(0, true);
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        _controlEventQueue.add(e);
        _semaphore.release();
    }

    public void loop() throws InterruptedException {
        while (true) {
            while (!_controlEventQueue.isEmpty())
                if (!handleControlQueueItem(_controlEventQueue.removeFirst()))
                    return;

            if (!_dprocess.waitForEvents())
                return;

            try {
                _semaphore.acquire();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private boolean handleControlQueueItem(ControlEvent e) {
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
                _dprocess.addBreakpoint(e.className, e.argument);
            }
            break;
            case WATCHPOINT: {
                _dprocess.addWatchpoint(e.className, e.argument);
            }
            break;
            case SCREENSHOT: {
                _dprocess.screenShot(e.argument);
            }
            break;
            case STOP: {
                return false;
            }
        }
        return true;
    }

    public void run() throws InterruptedException {
        loop();
        dispose();
    }

    private void dispose() {
        _dprocess.dispose();

        _eventBus.post(new ProcessEvent(ProcessEventType.STOPPED));
        _eventBus.unregister(this);
    }
}