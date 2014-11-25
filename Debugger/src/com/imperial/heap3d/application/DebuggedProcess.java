package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.snapshot.StackNode;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.imperial.heap3d.application.ProcessState.*;

public class DebuggedProcess {

    private HeapGraphFactory _heapGraphFactory;
    private StartDefinition _definition;
    private IVirtualMachineProvider _provider;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private BreakpointManager _manager;
    private EventBus _eventBus;

    private ThreadReference _threadReference;

    public DebuggedProcess(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus, HeapGraphFactory heapGraphFactory) {
        _state = STOPPED;
        _definition = definition;
        _heapGraphFactory = heapGraphFactory;
        _provider = provider;
        _eventBus = eventBus;
        _eventBus.register(this);
    }

    public void dispose() {
        _state = STOPPED;
        _process.destroy();
    }

    public void start() {
        runProcessAndEstablishConnection();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new StreamListener(_eventBus, _process.getInputStream(), _process.getErrorStream()));
        service.shutdown();
    }

    private void runProcessAndEstablishConnection() {
        int port = getRandomPort();
        ConnectedProcess cp = _provider.establish(port, () -> _definition.buildProcess(port));
        _instance = cp.virtualMachine;
        _process = cp.process;
        _manager = new BreakpointManager(_instance);
        _state = RUNNING;
    }

    private int getRandomPort() {
        final int RANGE = 10000;
        final int MINIMUM = 30000;
        return ((int) Math.ceil(Math.random() * RANGE)) + MINIMUM;
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

    public void createStepRequest() {
        if (_threadReference != null && _state == PAUSED) {
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadReference, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
            sr.addCountFilter(1);
            sr.enable();
            resume();
            _state = RUNNING;
        }
    }

    public void addBreakpoint(String className, String argument) {
        _manager.addBreakpoint(className, argument);
    }

    public void addWatchpoint(String className, String argument) {
        _manager.addWatchpoint(className, argument);
    }
}