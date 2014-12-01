package tests.unit.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.application.BreakpointManager;
import com.imperial.heap3d.application.ConnectedProcess;
import com.imperial.heap3d.application.DebuggedProcess;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;
import java.util.LinkedList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by oskar on 29/11/14.
 */
public class DebuggedProcessTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private DebuggedProcess SystemUnderTest;

    private Process _process;
    private VirtualMachine _instance;
    private ConnectedProcess _connectedProcess;
    private BreakpointManager _breakpointManager;
    private HeapGraphFactory _heapGraphFactory;
    private EventBus _eventBus;

    @Before
    public void SetUp() {
        _process = createNiceMock(Process.class);
        _instance = createNiceMock(VirtualMachine.class);
        _connectedProcess = new ConnectedProcess(_instance, _process);
        _breakpointManager = createNiceMock(BreakpointManager.class);
        _heapGraphFactory = createNiceMock(HeapGraphFactory.class);
        _eventBus = createNiceMock(EventBus.class);

        SystemUnderTest = new DebuggedProcess(_connectedProcess, _breakpointManager,
                _heapGraphFactory, _eventBus);
        resetAll();
    }

    @Test
    public void Test_Constructor_InvalidArguments_ConnectedProcess_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new DebuggedProcess(null, _breakpointManager,
                _heapGraphFactory, _eventBus);
    }

    @Test
    public void Test_Constructor_InvalidArguments_BreakpointManager_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new DebuggedProcess(_connectedProcess, null,
                _heapGraphFactory, _eventBus);
    }

    @Test
    public void Test_Constructor_InvalidArguments_HeapGraphFactory_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new DebuggedProcess(_connectedProcess, _breakpointManager,
                null, _eventBus);
    }

    @Test
    public void Test_Constructor_InvalidArguments_EventBus_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new DebuggedProcess(_connectedProcess, _breakpointManager,
                _heapGraphFactory, null);
    }

    @Test
    public void Test_Dispose_PostStoppedEvent() {
        //arrange
        _eventBus.post(anyObject(ProcessEvent.class));
        _process.destroy();
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.dispose();

        //assert
        verifyAll();
    }

    @Test
    public void Test_PauseWhenRunning_SuspendsInstance() {
        //arrange
        _instance.suspend();
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.pause();

        //assert
        verifyAll();
    }

    @Test
    public void Test_PauseWhenPaused_NoCall() {
        //arrange
        _instance.suspend();
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.pause();
        SystemUnderTest.pause();

        //assert
        verifyAll();
    }

    @Test
    public void Test_ResumeWhenRunning_NoCall() {
        //arrange
        replayAll();

        //act
        SystemUnderTest.resume();

        //assert
        verifyAll();
    }

    @Test
    public void Test_ResumeWhenPaused_ResumeInstance() {
        //arrange
        _instance.resume();
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.pause();
        SystemUnderTest.resume();

        //assert
        verifyAll();
    }

    @Test
    public void Test_WaitForEvents_WhenPaused_NoCall() {
        //arrange
        SystemUnderTest.pause();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        assertTrue(result);

    }

    @Test
    public void Test_WaitForEvents_NoEvent_ReturnImmediately() throws InterruptedException {
        //arrange
        EventQueue eventQueue = createMock(EventQueue.class);
        expect(_instance.eventQueue()).andReturn(eventQueue);
        expect(eventQueue.remove(anyInt()))
                .andThrow(new InterruptedException());
        replayAll();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        verifyAll();
        assertTrue(result);
    }

    @Test
    public void Test_WaitForEvents_ResumeIfNotPausedByEvent() throws InterruptedException {
        //arrange
        EventSet eventSet = createEventExpectation(new LinkedList<Event>().iterator());
        eventSet.resume();
        expectLastCall().once();
        replayAll();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        verifyAll();
        assertTrue(result);
    }

    private EventSet createEventExpectation(Iterator<Event> it) throws InterruptedException {
        EventQueue eventQueue = createMock(EventQueue.class);
        EventSet eventSet = createMock(EventSet.class);
        expect(_instance.eventQueue()).andReturn(eventQueue);
        expect(eventQueue.remove(anyInt())).andReturn(eventSet);
        expect(eventSet.iterator()).andReturn(it);
        return eventSet;
    }

    @Test
    public void Test_WaitForEvents_VMDeathEvent_ReturnsFalse() throws InterruptedException {
        //arrange
        VMDeathEvent e = createMock(VMDeathEvent.class);
        LinkedList<Event> eventList = new LinkedList<>();
        eventList.add(e);
        createEventExpectation(eventList.iterator());
        replayAll();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        verifyAll();
        assertFalse(result);
    }

    @Test
    public void Test_WaitForEvents_ClassPrepareEvent_NotifyBreakpointManager() throws InterruptedException {
        //arrange
        ClassPrepareEvent e = createMock(ClassPrepareEvent.class);
        ReferenceType rt = createMock(ReferenceType.class);
        LinkedList<Event> eventList = new LinkedList<>();
        eventList.add(e);
        EventSet eventSet = createEventExpectation(eventList.iterator());

        expect(e.referenceType()).andReturn(rt);
        _breakpointManager.notifyClassLoaded(rt);
        expectLastCall().once();
        eventSet.resume();
        expectLastCall().once();
        replayAll();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        verifyAll();
        assertTrue(result);
    }

    @Ignore
    @Test
    public void Test_WaitForEvents_LocatableEvent_PausesExecution() throws InterruptedException {
        //arrange
        LocatableEvent e = createNiceMock(StepEvent.class);
        LinkedList<Event> eventList = new LinkedList<>();
        eventList.add(e);
        EventSet eventSet = createEventExpectation(eventList.iterator());

        replayAll();

        //act
        boolean result = SystemUnderTest.waitForEvents();

        //assert
        verifyAll();
        assertTrue(result);
    }
}