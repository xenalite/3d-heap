package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.events.ControlEvent;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.ProcessEventType;
import com.imperial.heap3d.utilities.Check;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

/**
 * Created by oskar on 31/10/14.
 */
public class ControlEventHandler {

    private DebuggedProcess _dprocess;
    private BreakpointManager _breakpointManager;
    private EventBus _eventBus;
    private ConcurrentLinkedDeque<ControlEvent> _controlEventQueue;
    private Semaphore _semaphore;

    public ControlEventHandler(DebuggedProcess debuggedProcess, EventBus eventBus, BreakpointManager breakpointManager) {
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _semaphore = new Semaphore(1, true);

        _dprocess = Check.NotNull(debuggedProcess, "debuggedProcess");
        _breakpointManager = Check.NotNull(breakpointManager, "breakpointManager");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _eventBus.register(this);
        _eventBus.post(new ProcessEvent(ProcessEventType.STARTED));
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        _controlEventQueue.add(e);
        _semaphore.release();
    }

    private void loop() throws InterruptedException {
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
            case PAUSE: {
                _dprocess.pause();
            }
            break;
            case RESUME: {
                _dprocess.resume();
            }
            break;
            case STEPOVER: {
                _dprocess.createStepOverRequest();
            }
            break;
            case STEPINTO: {
                _dprocess.createStepIntoRequest();
            }
            break;
            case STEPOUT: {
                _dprocess.createStepOutRequest();
            }
            break;
            case BREAKPOINT: {
                _breakpointManager.addBreakpoint(e.className, e.argument);
            }
            break;
            case RMBREAKPOINT: {
                _breakpointManager.removeBreakpoint(e.className, e.argument);
            }
            break;
            case WATCHPOINT: {
                _breakpointManager.addWatchpoint(e.className, e.argument);
            }
            break;
            case RMWATCHPOINT: {
                _breakpointManager.removeWatchpoint(e.className, e.argument);
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
        _eventBus.unregister(this);
        _dprocess.dispose();
    }
}