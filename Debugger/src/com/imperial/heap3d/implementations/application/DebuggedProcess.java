package com.imperial.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.application.IBreakpointManager;
import com.imperial.heap3d.interfaces.application.IDebuggedProcess;
import com.imperial.heap3d.interfaces.application.IStepManager;
import com.imperial.heap3d.interfaces.application.IVariableAnalyser;
import com.imperial.heap3d.interfaces.jdi.IEventSet;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.event.*;

import static com.imperial.heap3d.implementations.application.ProcessState.*;

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
        _variableAnalyser = Check.NotNull(variableAnalyser, "variableAnalyser");
        _process = Check.NotNull(process, "process");
        _breakpointManager = Check.NotNull(breakpointManager, "breakpointManager");
        _stepManager = Check.NotNull(stepManager, "stepManager");
        _generalEventBus = Check.NotNull(generalEventBus, "generalEventBus");
        _processStateEventBus = Check.NotNull(processStateEventBus, "processStateEventBus");
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
            System.out.println("handle events -- running");
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
        _stepManager.notifyPausedAtLocation(event.thread());

        _generalEventBus.post(new ProcessEvent(ProcessEventType.DEBUG_MSG, String.format(format, event.thread().name(),
                event.location().declaringType().name(), event.location().method(), event.location().lineNumber())));

        try {
            _variableAnalyser.analyseVariables(event.thread().frame(0));
        } catch (IncompatibleThreadStateException ignored) {}
    }
}