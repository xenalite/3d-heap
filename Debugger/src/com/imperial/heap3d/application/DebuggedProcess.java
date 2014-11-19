package com.imperial.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.imperial.heap3d.application.ProcessState.*;
import static com.imperial.heap3d.events.ProcessEventType.DEBUG_MSG;
import static java.util.Map.Entry;

/**
 * Created by oskar on 11/11/14.
 */
public class DebuggedProcess {

    private StartDefinition _definition;
    private IVirtualMachineProvider _provider;
    private VirtualMachine _instance;
    private Process _process;
    private ProcessState _state;
    private BreakpointManager _manager;
    private EventBus _eventBus;

    private ThreadReference _threadRef;

    private Set<Node> allHeapNodes = new HashSet<>();
    private Stack<StackNode> stackNodes = new Stack<>();

    public DebuggedProcess(StartDefinition definition, IVirtualMachineProvider provider, EventBus eventBus) {
        _definition = definition;
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
            _threadRef = null;
            _instance.resume();
        }
    }

    public boolean waitForEvents() throws InterruptedException {
        if(_instance != null && _state == RUNNING) {
            EventQueue vmQueue = _instance.eventQueue();
            EventSet set = vmQueue.remove();
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

    // -- TODO REFACTOR THIS INTO A SEPARATE INTERFACE
    private static final String delim = "------\n";
    private void analyseVariables(LocatableEvent e) {
        stackNodes.clear();
        allHeapNodes.clear();
        try {
            StringBuilder sb = new StringBuilder();
            ThreadReference threadReference = e.thread();
            Location location = e.location();
            ReferenceType referenceType = location.declaringType();
            StackFrame stackFrame = threadReference.frame(0);
            ObjectReference thisObject = stackFrame.thisObject();

            List<LocalVariable> localVariables = stackFrame.visibleVariables();
            Map<LocalVariable, Value> valuesOfLocalVars = stackFrame.getValues(localVariables);
            sb.append(delim);
            sb.append("Local variables:\n");
            for(Entry<LocalVariable, Value> entry : valuesOfLocalVars.entrySet()) {
                LocalVariable lv = entry.getKey();
                Value v = entry.getValue();
                sb.append(String.format("%s (%s) = %s\n", lv.name(), lv.typeName(), v));
                walkHeap(lv.name(), v, sb);
            }

            System.out.println("============================");
            for (Node node : allHeapNodes) {
                System.out.println(node.getName());
            }

            List<Field> allFields = referenceType.fields();
            if(thisObject != null) {
                Map<Field, Value> valuesOfFields = thisObject.getValues(allFields);
                sb.append(delim);
                sb.append("Instance variables:\n");
                for(Entry<Field, Value> entry : valuesOfFields.entrySet()) {
                    Field f = entry.getKey();
                    Value v = entry.getValue();
                    String typeName = (f.isStatic()) ? "static " + f.typeName() : f.typeName();
                    sb.append(String.format("%s (%s) = %s\n", f.name(), typeName, v));
                    walkHeap(f.name(), v, sb);
                }
            }
            else {
                List<Field> staticFields = allFields.stream().filter(TypeComponent::isStatic)
                        .collect(Collectors.toCollection(LinkedList::new));

                Map<Field, Value> valuesOfFields = referenceType.getValues(staticFields);
                sb.append(delim);
                sb.append("Static variables:\n");
                for(Entry<Field, Value> entry : valuesOfFields.entrySet()) {
                    Field f = entry.getKey();
                    Value v = entry.getValue();
                    sb.append(String.format("%s (%s) = %s\n", f.name(), f.typeName(), v));
                    walkHeap(f.name(), v, sb);
                }
            }
            _eventBus.post(new ProcessEvent(DEBUG_MSG, sb.toString()));
        }
        catch(Exception ex) {
            System.out.println("Exception");
        }
    }

    private void walkHeap(String name, Value v, StringBuilder sb){
    	
        StackNode stackNode = null;
        
        //TODO I think this is wrong... Task in Sonic for this
        if (v instanceof ArrayReference){
        	ArrayReference arrayRef = (ArrayReference) v;
        	HeapNode heapNode = new HeapNode("NULL", arrayRef.uniqueID());
        	
        	 /* Check for cycles in the heap nodes. */
            if (!allHeapNodes.contains(heapNode)) {
                heapNode = drillDownArrayReference(heapNode, arrayRef, sb);
                stackNode = new StackNode(name, heapNode);
                allHeapNodes.add(heapNode);
            }
            
        } else if (v instanceof ObjectReference) {
            ObjectReference objRef = (ObjectReference) v;
            HeapNode heapNode = new HeapNode("NULL", objRef.uniqueID());

            /* Check for cycles in the heap nodes. */
            if (!allHeapNodes.contains(heapNode)) {
                heapNode = drillDownObjectReference(heapNode, objRef, sb);
                stackNode = new StackNode(name, heapNode);
                allHeapNodes.add(heapNode);
            }

        } else {
            stackNode = new StackNode(name, v);
        }

        stackNodes.push(stackNode);
    }
    
    private HeapNode drillDownObjectReference(HeapNode parent, ObjectReference objectValue, StringBuilder sb) {

        ReferenceType referenceType = objectValue.referenceType();
        List<Field> fields = referenceType.fields();
        Map<Field, Value> valuesOfFields = objectValue.getValues(fields);

        for (Entry<Field, Value> entry : valuesOfFields.entrySet()) {
            Field f = entry.getKey();
            Value v = entry.getValue();
            String typeName = (f.isStatic()) ? "static " + f.typeName() : f.typeName();
            sb.append(String.format("\t %s (%s) = %s\n", f.name(), typeName, v));

            if(v instanceof ObjectReference){
                ObjectReference objRef = (ObjectReference) v;
                HeapNode childNode = new HeapNode(f.name(), objRef.uniqueID());
                childNode = drillDownObjectReference(childNode, objRef, sb);
                allHeapNodes.add(childNode);
                parent.addHeapNodeRef(childNode);
            }else{
            	// Its a primitive or a collection (i,e, ArrayRef)
                // TODO :: for ArrayRef
                parent.setChildPrimitive(f.name(), v);
            }
        }
        return parent;
    }

    private HeapNode drillDownArrayReference(HeapNode parent, ArrayReference arrayValue, StringBuilder sb) {

    	List<Value> arrayValues = arrayValue.getValues();
    	int i = 0;
    	for(Value valueEntry : arrayValues) {
    		sb.append(String.format("\t [%d] (%s) = %s\n", i, valueEntry.type().name(), arrayValue));
    		++i;
    		if(valueEntry instanceof ObjectReference){
    			System.err.println("here--------------");
                 ObjectReference objRef = (ObjectReference) valueEntry;
                 HeapNode childNode = new HeapNode(valueEntry.type().name(), objRef.uniqueID());
                 childNode = drillDownObjectReference(childNode, objRef, sb);
                 allHeapNodes.add(childNode);
                 parent.addHeapNodeRef(childNode);
             }
    	}
    	return parent;
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