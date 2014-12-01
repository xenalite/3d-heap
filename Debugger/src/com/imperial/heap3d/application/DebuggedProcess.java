package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.NodeEvent;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.ProcessEventType;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.NodesBuilder;
import com.imperial.heap3d.snapshot.StackNode;
import com.imperial.heap3d.utilities.Check;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

import java.util.Collection;

import static com.imperial.heap3d.application.ProcessState.*;

public class DebuggedProcess {

    private HeapGraphFactory _heapGraphFactory;
    private EventBus _eventBus;
    private NodesBuilder _builder;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private BreakpointManager _manager;
    private ThreadReference _threadReference;

    public DebuggedProcess(ConnectedProcess connectedProcess, BreakpointManager breakpointManager,
                           HeapGraphFactory heapGraphFactory, EventBus eventBus) {
        _state = RUNNING;
        _heapGraphFactory = Check.NotNull(heapGraphFactory, "heapGraphFactory");
        connectedProcess = Check.NotNull(connectedProcess, "connectedProcess");
        _process = Check.NotNull(connectedProcess.getProcess(), "connectedProcess.process");
        _instance = Check.NotNull(connectedProcess.getVirtualMachine(), "connectedProcess.virtualMachine");
        _manager = Check.NotNull(breakpointManager, "breakpointManager");
        _eventBus = Check.NotNull(eventBus, "eventBus");
        _builder = new NodesBuilder();
    }

    public void dispose() {
        _state = STOPPED;
        _eventBus.post(new ProcessEvent(ProcessEventType.STOPPED));
        _process.destroy();
    }

    public void pause() {
        if (_state == RUNNING) {
            _state = PAUSED;
            _instance.suspend();
        }
    }

    public void resume() {
        if (_state == PAUSED || _state == PAUSED_AT_LOCATION) {
            _state = RUNNING;
            _threadReference = null;
            _instance.resume();
        }
    }

    public boolean waitForEvents() {
        if (_state == RUNNING) {
            EventQueue vmQueue = _instance.eventQueue();
            EventSet set;
            try {
                set = vmQueue.remove(0);
            } catch (InterruptedException e) {
                return true;
            }
            for (Event e : set) {
                System.out.println(e);
                if (e instanceof VMDeathEvent)
                    return false;
                else if (e instanceof ClassPrepareEvent)
                    _manager.notifyClassLoaded(((ClassPrepareEvent) e).referenceType());
                else if (e instanceof ModificationWatchpointEvent)
                    hitLocatableEvent((LocatableEvent) e, "Watchpoint hit at:\n" +
                            "[Thread]:%s\n" +
                            "[Class]:%s\n" +
                            "[Method]:%s\n" +
                            "[Line #]:%s");
                else if (e instanceof BreakpointEvent)
                    hitLocatableEvent((LocatableEvent) e, "Breakpoint hit at:\n" +
                            "[Thread]:%s\n" +
                            "[Class]:%s\n" +
                            "[Method]:%s\n" +
                            "[Line #]:%s");
                else if (e instanceof StepEvent)
                    hitLocatableEvent((LocatableEvent) e, "Stepped to:\n" +
                            "[Thread]:%s\n" +
                            "[Class]:%s\n" +
                            "[Method]:%s\n" +
                            "[Line #]:%s");
            }
            if (_state == RUNNING)
                set.resume();
        }
        return true;
    }

    private void hitLocatableEvent(LocatableEvent event, String format) {
        _state = PAUSED_AT_LOCATION;
        _threadReference = event.thread();
        removeStepRequests();
        _eventBus.post(new ProcessEvent(ProcessEventType.DEBUG_MSG, String.format(format, event.thread().name(),
                event.location().declaringType().name(), event.location().method(), event.location().lineNumber())));
        analyseVariables(event);
    }

    private void analyseVariables(LocatableEvent event) {
        Collection<StackNode> stackNodes;
        StackFrame stackFrame;
        try {
            stackFrame = event.thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            return;
        }
        stackNodes = _builder.build(stackFrame);
        _heapGraphFactory.create().giveStackNodes(stackNodes);
        _eventBus.post(new NodeEvent(stackNodes));
    }

    private void removeStepRequests() {
        _instance.eventRequestManager().stepRequests().stream()
                .filter(sr -> _threadReference.equals(sr.thread()))
                .forEach(EventRequest::disable);
    }

    public void createStepOverRequest() {
        createStepRequest(StepRequest.STEP_LINE, StepRequest.STEP_OVER);
    }

    public void createStepIntoRequest() {
        createStepRequest(StepRequest.STEP_MIN, StepRequest.STEP_INTO);
    }

    public void createStepOutRequest() {
        createStepRequest(StepRequest.STEP_LINE, StepRequest.STEP_OUT);
    }

    private void createStepRequest(int size, int depth) {
        if (_threadReference != null && _state == PAUSED_AT_LOCATION) {
            removeStepRequests();
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadReference, size, depth);
            sr.addCountFilter(1);
            sr.enable();
            resume();
            _state = RUNNING;
        }
    }

    public void screenShot(String path) {
        java.io.File f = new java.io.File(path);
        _heapGraphFactory.create().screenShot(f.getParent(), f.getName());
    }
}