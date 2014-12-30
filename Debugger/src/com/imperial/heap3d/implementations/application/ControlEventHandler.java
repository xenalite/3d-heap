package com.imperial.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.events.ControlEvent;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.utilities.Check;
import com.imperial.heap3d.interfaces.application.IBreakpointManager;
import com.imperial.heap3d.interfaces.application.IDebuggedProcess;
import com.imperial.heap3d.interfaces.application.IStepManager;
import com.imperial.heap3d.interfaces.jdi.IVirtualMachine;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by oskar on 31/10/14.
 */
public class ControlEventHandler {

    private IVirtualMachine _virtualMachine;
    private IStepManager _stepManager;
    private IDebuggedProcess _dprocess;
    private IBreakpointManager _breakpointManager;
    private EventBus _eventBus;
    private ConcurrentLinkedDeque<ControlEvent> _controlEventQueue;

    public ControlEventHandler(IDebuggedProcess debuggedProcess, EventBus eventBus, IVirtualMachine virtualMachine,
                               IBreakpointManager breakpointManager, IStepManager stepManager) {
        _controlEventQueue = new ConcurrentLinkedDeque<>();

        _dprocess = Check.notNull(debuggedProcess, "debuggedProcess");
        _breakpointManager = Check.notNull(breakpointManager, "breakpointManager");
        _stepManager = Check.notNull(stepManager, "stepManager");
        _virtualMachine = Check.notNull(virtualMachine, "virtualMachine");
        _eventBus = Check.notNull(eventBus, "eventBus");
        _eventBus.register(this);
        _eventBus.post(new ProcessEvent(ProcessEventType.STARTED));
    }

    @Subscribe
    public void handleEvent(ControlEvent e) {
        _controlEventQueue.add(e);
    }

    private void loop() throws InterruptedException {
        while (true) {
            while (!_controlEventQueue.isEmpty())
                if (!handleControlQueueItem(_controlEventQueue.removeFirst()))
                    return;

            if (!_dprocess.handleEvents(_virtualMachine.getEventQueue()))
                return;
        }
    }

    private boolean handleControlQueueItem(ControlEvent e) {
        switch (e.type) {
            case PAUSE: {
                _virtualMachine.pause();
            }
            break;
            case RESUME: {
                _virtualMachine.resume();
            }
            break;
            case STEPOVER: {
                _stepManager.createStepOverRequest();
            }
            break;
            case STEPINTO: {
                _stepManager.createStepIntoRequest();
            }
            break;
            case STEPOUT: {
                _stepManager.createStepOutRequest();
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