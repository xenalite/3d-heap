package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.layout.HeapGraph;
import com.imperial.heap3d.snapshot.*;
import com.imperial.heap3d.ui.controllers.MainWindowController;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.imperial.heap3d.application.ProcessState.*;
import static com.imperial.heap3d.events.ProcessEventType.DEBUG_MSG;
import static java.util.Map.Entry;

public class DebuggedProcess {

    private StartDefinition _definition;
    private IVirtualMachineProvider _provider;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private BreakpointManager _manager;
    private EventBus _eventBus;

    private ThreadReference _threadRef;
    private Snapshot _snapshot;

    private Thread renderThread = null;
    private HeapGraph heapGraphRender = null;
    
    public DebuggedProcess(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _definition = definition;
        _provider = provider;
        _eventBus = eventBus;
        _eventBus.register(this);
        _snapshot = new Snapshot();
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
            _threadRef = null;
            _instance.resume();
        }
    }

    public boolean waitForEvents() {
        if(_instance != null && _state == RUNNING) {
            EventQueue vmQueue = _instance.eventQueue();
            EventSet set = null;
            try {
                set = vmQueue.remove(0);
            } catch (InterruptedException e) { return true; }
            for(Event e : set) {
                System.out.println(e);
                if(e instanceof VMDeathEvent) {
                    return false;
                }
                else if(e instanceof ClassPrepareEvent) {
                    ReferenceType classReference = ((ClassPrepareEvent) e).referenceType();
                    _manager.notifyClassLoaded(classReference);
                }
                else if(e instanceof ModificationWatchpointEvent) {
                    _state = PAUSED;
                    ModificationWatchpointEvent mwe = (ModificationWatchpointEvent) e;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG,
                            String.format("Variable %s in %s modified! Old:%s, New:%s",
                                    mwe.field(), mwe.location(), mwe.valueCurrent(), mwe.valueToBe())));
                    _threadRef = mwe.thread();
                    removeStepRequests();
                    analyseVariables(mwe);
                }
                else if(e instanceof BreakpointEvent) {
                    _state = PAUSED;
                    BreakpointEvent be = (BreakpointEvent) e;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG,
                            String.format("Breakpoint hit @%s", be.location())));
                    _threadRef = be.thread();
                    removeStepRequests();
                    analyseVariables(be);
                }
                else if(e instanceof StepEvent) {
                    StepEvent se = (StepEvent) e;
                    _threadRef = se.thread();
                    _state = PAUSED;
                    _eventBus.post(new ProcessEvent(DEBUG_MSG,
                            String.format("Step into @%s", se.location())));
                    e.request().disable();
                    analyseVariables((LocatableEvent) e);
                }
            }
            if(_state == RUNNING)
                set.resume();
        }
        return true;
    }

    private void analyseVariables(LocatableEvent event) {
        _snapshot.clear();

        try {
            StackFrame stackFrame = event.thread().frame(0);
            processLocalVariables(stackFrame);

            // TODO: Write toString() for Snapshot and pass it in here.
            //_eventBus.post(new ProcessEvent(DEBUG_MSG, sb.toString()));
        }
        catch(Exception ex) {
            System.out.println("Exception");
        }
        
        if(renderThread != null){
        	heapGraphRender.giveStackNodes(_snapshot.getStackNodes());
        }else{
        	heapGraphRender = new HeapGraph(_snapshot.getStackNodes());
        	renderThread = new Thread(heapGraphRender, "lwjgl");
            renderThread.start();
        }
    }
    
    
    private void processLocalVariables(StackFrame stackFrame) throws AbsentInformationException {

        ObjectReference thisObject = stackFrame.thisObject();

        if (thisObject != null) {
            String name = "this";
            _snapshot.addStackNode(new StackNode(name, drillDown(name, thisObject)));
        }

        for (Entry<LocalVariable, Value> entry : stackFrame.getValues(stackFrame.visibleVariables()).entrySet()) {
            String name = entry.getKey().name();
            Value value = entry.getValue();

            if (value instanceof PrimitiveValue) {
                _snapshot.addStackNode(new StackNode(name, processValue(value)));
            } else {
                _snapshot.addStackNode(new StackNode(name, drillDown(name, value)));
            }
        }
    }

    private static Node drillDown(String name, Value value) {
        ObjectReference reference = (ObjectReference) value;
        long id = reference.uniqueID();

        Node node;
        if (reference instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) reference;
            node = new ArrayNode(name, id);
            List<Value> arrayValues = arrayReference.getValues();
            for (int index = 0; index < arrayValues.size(); ++index) {
                Value arrayValue = arrayValues.get(index);
                addArrayNodeValue((ArrayNode) node, index, arrayValue);
            }
        } else if (reference instanceof StringReference) {
            StringReference stringReference = (StringReference) reference;
            node = new StringNode(name, id, stringReference.toString());
        } else {
            node = new HeapNode(name, id);
            for (Entry<Field, Value> entry : reference.getValues(reference.referenceType().allFields()).entrySet()) {
                String fieldName = entry.getKey().name();
                Value fieldValue = entry.getValue();
                addHeapNodeValue((HeapNode) node, fieldName, fieldValue);
            }
        }

        return node;
    }

    private static void addArrayNodeValue(ArrayNode node, int index, Value value) {
        if (value instanceof PrimitiveValue) {
            node.addPrimitive(processValue(value));
        } else if (value instanceof ArrayReference) {
            node.addArray((ArrayNode) drillDown(arrayIndexToString(node.getName(), index), value));
        } else if (value instanceof StringReference) {
            node.addString((StringNode) drillDown(arrayIndexToString(node.getName(), index), value));
        } else {
            node.addReference((HeapNode) drillDown(arrayIndexToString(node.getName(), index), value));
        }
    }

    private static String arrayIndexToString(String name, int index) {
        return name + "[" + String.valueOf(index) + "]";
    }

    private static void addHeapNodeValue(HeapNode node, String name, Value value) {
        if (value instanceof PrimitiveValue) {
            node.addPrimitive(name, processValue(value));
        } else if (value instanceof ArrayReference) {
            node.addArray((ArrayNode) drillDown(name, value));
        } else if (value instanceof StringReference) {
            node.addString((StringNode) drillDown(name, value));
        } else {
            node.addReference((HeapNode) drillDown(name, value));
        }
    }

    private static Object processValue(Value value) {
        PrimitiveValue primitive = (PrimitiveValue) value;
        if (primitive instanceof BooleanValue) {
            return ((BooleanValue) primitive).value();
        }

        if (primitive instanceof ByteValue) {
            return ((ByteValue) primitive).value();
        }

        if (primitive instanceof CharValue) {
            return ((CharValue) primitive).value();
        }

        if (primitive instanceof DoubleValue) {
            return ((DoubleValue) primitive).value();
        }

        if (primitive instanceof FloatValue) {
            return ((FloatValue) primitive).value();
        }

        if (primitive instanceof IntegerValue) {
            return ((IntegerValue) primitive).value();
        }

        if (primitive instanceof LongValue) {
            return ((LongValue) primitive).value();
        }

        if (primitive instanceof ShortValue) {
            return ((ShortValue) primitive).value();
        }

        return null;
    }

    private void removeStepRequests() {
        _instance.eventRequestManager().stepRequests().stream()
                .filter(sr -> _threadRef.equals(sr.thread()))
                .forEach(EventRequest::disable);
    }

    public void createStepRequest() {
        if(_threadRef != null && _state == PAUSED) {
            EventRequestManager erm = _instance.eventRequestManager();
            StepRequest sr = erm.createStepRequest(_threadRef, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
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