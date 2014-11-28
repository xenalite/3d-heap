package com.imperial.heap3d.application;

import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.NodesBuilder;
import com.imperial.heap3d.snapshot.StackNode;
import com.imperial.heap3d.utilities.Check;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

import java.util.Collection;
import java.util.LinkedList;

import static com.imperial.heap3d.application.ProcessState.*;

public class DebuggedProcess {

    private HeapGraphFactory _heapGraphFactory;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private BreakpointManager _manager;
    private ThreadReference _threadReference;

    public DebuggedProcess(ConnectedProcess connectedProcess, BreakpointManager breakpointManager, HeapGraphFactory heapGraphFactory) {
        _state = RUNNING;
        _heapGraphFactory = Check.NotNull(heapGraphFactory);
        _process = Check.NotNull(connectedProcess.getProcess());
        _instance = Check.NotNull(connectedProcess.getVirtualMachine());
        _manager = Check.NotNull(breakpointManager);
    }

    public void dispose() {
        _state = STOPPED;
        _process.destroy();
    }

    public void start() {

    }

    public void pause() {
        if (_instance != null && _state == RUNNING) {
            _state = PAUSED;
            _instance.suspend();
        }
    }

    public void resume() {
        if (_instance != null && _state == PAUSED) {
            _state = RUNNING;
            _threadReference = null;
            _instance.resume();
        }
    }

    public boolean waitForEvents() {
        if (_instance != null && _state == RUNNING) {
            EventQueue vmQueue = _instance.eventQueue();
            EventSet set;
            try {
                set = vmQueue.remove(0);
            } catch (InterruptedException e) {
                return true;
            }
            for (Event e : set) {
                System.out.println(e);
                if (e instanceof VMDeathEvent) {
                    return false;
                } else if (e instanceof ClassPrepareEvent) {
                    ReferenceType classReference = ((ClassPrepareEvent) e).referenceType();
                    _manager.notifyClassLoaded(classReference);
                } else if (e instanceof ModificationWatchpointEvent) {
                    _state = PAUSED;
                    ModificationWatchpointEvent mwe = (ModificationWatchpointEvent) e;
                    _threadReference = mwe.thread();
                    removeStepRequests();
                    analyseVariables(mwe);
                } else if (e instanceof BreakpointEvent) {
                    _state = PAUSED;
                    BreakpointEvent be = (BreakpointEvent) e;
                    _threadReference = be.thread();
                    removeStepRequests();
                    analyseVariables(be);
                } else if (e instanceof StepEvent) {
                    StepEvent se = (StepEvent) e;
                    _threadReference = se.thread();
                    _state = PAUSED;
                    e.request().disable();
                    analyseVariables((LocatableEvent) e);
                }
            }
            if (_state == RUNNING)
                set.resume();
        }
        return true;
    }

    private void analyseVariables(LocatableEvent event) {
        Collection<StackNode> stackNodes = new LinkedList<>();
        try {
            StackFrame stackFrame = event.thread().frame(0);
            stackNodes = new NodesBuilder(stackFrame).build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _heapGraphFactory.create().giveStackNodes(stackNodes);
    }

    private void removeStepRequests() {
        _instance.eventRequestManager().stepRequests().stream()
                .filter(sr -> _threadReference.equals(sr.thread()))
                .forEach(EventRequest::disable);
    }

    public void createStepOverRequest() {

        if (_threadReference != null && _state == PAUSED) {
            removeStepRequests();
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadReference, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
            sr.addCountFilter(1);
            sr.enable();
            resume();
            _state = RUNNING;
        }
    }

    public void createStepIntoRequest() {
        if (_threadReference != null && _state == PAUSED) {
            removeStepRequests();
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadReference, StepRequest.STEP_MIN, StepRequest.STEP_INTO);
            sr.addCountFilter(1);
            sr.enable();
            resume();
            _state = RUNNING;

        }
    }

    public void createStepOutRequest() {

        if (_threadReference != null && _state == PAUSED) {
            removeStepRequests();
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadReference, StepRequest.STEP_LINE, StepRequest.STEP_OUT);
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