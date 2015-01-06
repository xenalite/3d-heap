package com.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.ProcessEventType;
import com.heap3d.utilities.Check;
import com.heap3d.interfaces.application.IBreakpointManager;
import com.heap3d.interfaces.application.IDebuggedProcess;
import com.heap3d.interfaces.application.IStepManager;
import com.heap3d.interfaces.application.IVariableAnalyser;
import com.sun.jdi.event.*;

import static com.heap3d.implementations.application.ProcessState.*;

public class DebuggedProcess implements IDebuggedProcess {

    private EventBus _processStateEventBus;
    private IVariableAnalyser _variableAnalyser;
    private EventBus _generalEventBus;
    private Process _process;
    private ProcessState _state;
    private IBreakpointManager _breakpointManager;
    private IStepManager _stepManager;

    private static final String CommonFormat =
            "[Thread]:%s\n" +
            "[Class]:%s\n" +
            "[Method]:%s\n" +
            "[Line #]:%s";

    public DebuggedProcess(Process process, IBreakpointManager breakpointManager, IStepManager stepManager,
                           IVariableAnalyser variableAnalyser, EventBus generalEventBus, EventBus processStateEventBus) {
        _state = RUNNING;
        _variableAnalyser = Check.notNull(variableAnalyser, "variableAnalyser");
        _process = Check.notNull(process, "process");
        _breakpointManager = Check.notNull(breakpointManager, "breakpointManager");
        _stepManager = Check.notNull(stepManager, "stepManager");
        _generalEventBus = Check.notNull(generalEventBus, "generalEventBus");
        _processStateEventBus = Check.notNull(processStateEventBus, "processStateEventBus");
        _processStateEventBus.register(this);
    }

    @Override
    public void dispose() {
        _processStateEventBus.unregister(this);
        _processStateEventBus.post(STOPPED);
        _generalEventBus.post(new ProcessEvent(ProcessEventType.STOPPED));
        _process.destroy();
    }

    @Subscribe
    @Override
    public void notifyStateChanged(ProcessState newState) {
        _state = newState;
    }

    @Override
    public boolean handleEvents(EventQueue eventQueue) {
        if(_state == RUNNING) {

            EventSet eventSet;
            try {
                eventSet = eventQueue.remove(0);
            } catch (InterruptedException e) {return true;}
            for (Event e : eventSet) {
                System.out.println(e);
                if (e instanceof VMDeathEvent)
                    return false;
                else if (e instanceof ClassPrepareEvent)
                    _breakpointManager.notifyClassLoaded(((ClassPrepareEvent) e).referenceType());
                else if (e instanceof ModificationWatchpointEvent)
                    hitLocatableEvent((LocatableEvent) e, "Watchpoint hit at:\n" + CommonFormat);
                else if (e instanceof BreakpointEvent)
                    hitLocatableEvent((LocatableEvent) e, "Breakpoint hit at:\n" + CommonFormat);
                else if (e instanceof StepEvent)
                    hitLocatableEvent((LocatableEvent) e, "Stepped to:\n" + CommonFormat);
            }
            if (_state == RUNNING)
                eventSet.resume();
        }
        return true;
    }

    private void hitLocatableEvent(LocatableEvent event, String format) {
        _processStateEventBus.post(PAUSED_AT_LOCATION);
        _stepManager.notifyPausedAtLocation(event);

        _generalEventBus.post(new ProcessEvent(ProcessEventType.DEBUG_MSG, String.format(format, event.thread().name(),
                event.location().declaringType().name(), event.location().method(), event.location().lineNumber())));

        _variableAnalyser.analyseVariables(event);
    }
}