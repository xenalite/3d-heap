package tests.unit.viewmodels;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.events.ControlEvent;
import com.imperial.heap3d.implementations.events.EventType;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import com.imperial.heap3d.implementations.events.ProcessEventType;
import com.imperial.heap3d.implementations.viewmodels.BreakpointsTabViewModel;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointsViewModelTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private BreakpointsTabViewModel SystemUnderTest;
    private EventBus _mockEventBus;
    private static final String VALUE = "VALUE";

    @Before
    public void SetUp() {
        _mockEventBus = createMock(EventBus.class);
        _mockEventBus.register(eq(SystemUnderTest));
        SystemUnderTest = new BreakpointsTabViewModel(_mockEventBus);
        resetAll();
    }

    @Test
    public void Test_Constructor_WithInvalidArguments_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new BreakpointsTabViewModel(null);
    }

    @Ignore // for convenience only.
    @Test
    public void Test_Initially_NoBreakpointsDefined() {
        assertTrue(SystemUnderTest.getWatchpointsProperty().getValue().isEmpty());
        assertTrue(SystemUnderTest.getBreakpointsProperty().getValue().isEmpty());
        assertTrue(SystemUnderTest.getBreakpointClassProperty().getValue().isEmpty());
        assertTrue(SystemUnderTest.getWatchpointClassProperty().getValue().isEmpty());
        assertTrue(SystemUnderTest.getWatchpointFieldProperty().getValue().isEmpty());
        assertTrue(SystemUnderTest.getBreakpointMethodProperty().getValue().isEmpty());
    }

    //region Breakpoints
    @Test
    public void Test_AddBreakpoint_NoClassNameOrMethod_NotAdded() {
        //act
        SystemUnderTest.addBreakpointAction();

        //assert
        assertTrue(SystemUnderTest.getBreakpointsProperty().getValue().isEmpty());
        assertEquals("", SystemUnderTest.getBreakpointClassProperty().get());
        assertEquals("", SystemUnderTest.getBreakpointMethodProperty().get());
    }

    @Test
    public void Test_AddBreakpoint_NoMethodName_NotAdded() {
        //arrange
        SystemUnderTest.getBreakpointClassProperty().set(VALUE);
        //act
        SystemUnderTest.addBreakpointAction();

        //assert
        assertTrue(SystemUnderTest.getBreakpointsProperty().getValue().isEmpty());
        assertEquals(VALUE, SystemUnderTest.getBreakpointClassProperty().get());
        assertEquals("", SystemUnderTest.getBreakpointMethodProperty().get());
    }

    @Test
    public void Test_AddBreakpoint_NoClassName_NotAdded() {
        //arrange
        SystemUnderTest.getBreakpointMethodProperty().set(VALUE);
        //act
        SystemUnderTest.addBreakpointAction();

        //assert
        assertTrue(SystemUnderTest.getBreakpointsProperty().getValue().isEmpty());
        assertEquals("", SystemUnderTest.getBreakpointClassProperty().get());
        assertEquals(VALUE, SystemUnderTest.getBreakpointMethodProperty().get());
    }

    @Test
    public void Test_AddBreakpoint_ValidArguments_Added() {
        //arrange
        SystemUnderTest.getBreakpointClassProperty().set(VALUE);
        SystemUnderTest.getBreakpointMethodProperty().set(VALUE);

        //act
        SystemUnderTest.addBreakpointAction();

        //assert
        assertEquals(1, SystemUnderTest.getBreakpointsProperty().getValue().size());
        assertEquals("", SystemUnderTest.getBreakpointClassProperty().get());
        assertEquals("", SystemUnderTest.getBreakpointMethodProperty().get());
    }

    @Test
    public void Test_BreakpointsCached_ByDefault() {
        //expect -- no posts to event bus
        replayAll();
        //act
        setDefaultBreakpoint();

        //assert
        verifyAll();
    }


    @Test
    public void Test_BreakpointsSent_WhenProgramStarts() {
        //arrange
        setDefaultBreakpoint();
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        //expectation
        _mockEventBus.post(eq(e));
        expectLastCall().times(2);
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(2, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_BreakpointsCached_WhenProgramKilled() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
//        SystemUnderTest.handleEvent(e2);
        setDefaultBreakpoint();

        //assert
        assertEquals(2, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_BreakpointsRemembered_ConsecutiveRuns() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
//        SystemUnderTest.handleEvent(e2);
//        setDefaultBreakpoint();
//        SystemUnderTest.handleEvent(e1);

        //assert
        assertEquals(2, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_BreakpointsRemembered_ConsecutiveRuns_UsingProcessEvent() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ProcessEvent e2 = new ProcessEvent(ProcessEventType.STOPPED);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
//        SystemUnderTest.handleProcessEvent(e2);
//        setDefaultBreakpoint();
//        SystemUnderTest.handleEvent(e1);

        //assert
        assertEquals(2, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_RemoveBreakpoints_InvalidArgument_DoesntThrow() {
        //arrange
        setDefaultBreakpoint();

        //act
        SystemUnderTest.removeBreakpointAction("");

        //assert
        assertEquals(1, SystemUnderTest.getBreakpointsProperty().getValue().size());
    }

    @Test
    public void Test_RemoveBreakpoints_BeforeProgramRuns_NothingSent() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);
        replayAll();

        //act
//        SystemUnderTest.removeBreakpointAction(VALUE + ":" + VALUE);
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(0, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_RemoveBreakpoints_TwoEquals_OnlyOneRemoved() {
        //arrange
        setDefaultBreakpoint();
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().once();
        replayAll();

        //act
//        SystemUnderTest.removeBreakpointAction(VALUE + ":" + VALUE);
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(1, SystemUnderTest.getBreakpointsProperty().getValue().size());
        verifyAll();
    }
    //endregion

    //region Watchpoints
    @Test
    public void Test_AddWatchpoint_noClassNameOrMethod_NotAdded() {
        //act
        SystemUnderTest.addWatchpointAction();

        //assert
        assertTrue(SystemUnderTest.getWatchpointsProperty().getValue().isEmpty());
        assertEquals("", SystemUnderTest.getWatchpointClassProperty().get());
        assertEquals("", SystemUnderTest.getWatchpointFieldProperty().get());
    }

    @Test
    public void Test_AddWatchpoint_noMethodName_NotAdded() {
        //arrange
        SystemUnderTest.getWatchpointClassProperty().set(VALUE);
        //act
        SystemUnderTest.addWatchpointAction();

        //assert
        assertTrue(SystemUnderTest.getWatchpointsProperty().getValue().isEmpty());
        assertEquals(VALUE, SystemUnderTest.getWatchpointClassProperty().get());
        assertEquals("", SystemUnderTest.getWatchpointFieldProperty().get());
    }

    @Test
    public void Test_AddWatchpoint_noClassName_NotAdded() {
        //arrange
        SystemUnderTest.getWatchpointFieldProperty().set(VALUE);
        //act
        SystemUnderTest.addWatchpointAction();

        //assert
        assertTrue(SystemUnderTest.getWatchpointsProperty().getValue().isEmpty());
        assertEquals("", SystemUnderTest.getWatchpointClassProperty().get());
        assertEquals(VALUE, SystemUnderTest.getWatchpointFieldProperty().get());
    }

    @Test
    public void Test_AddWatchpoint_validArguments_Added() {
        //arrange
        SystemUnderTest.getWatchpointClassProperty().set(VALUE);
        SystemUnderTest.getWatchpointFieldProperty().set(VALUE);

        //act
        SystemUnderTest.addWatchpointAction();

        //assert
        assertEquals(1, SystemUnderTest.getWatchpointsProperty().getValue().size());
        assertEquals("", SystemUnderTest.getWatchpointClassProperty().get());
        assertEquals("", SystemUnderTest.getWatchpointFieldProperty().get());
    }

    @Test
    public void Test_WatchpointsCached_ByDefault() {
        //expect -- no posts to event bus
        replayAll();
        //arrange
        SystemUnderTest.getWatchpointFieldProperty().set(VALUE);
        SystemUnderTest.getWatchpointClassProperty().set(VALUE);

        //act
        SystemUnderTest.addWatchpointAction();

        //assert
        verifyAll();
    }



    @Test
    public void Test_WatchpointsSent_WhenProgramStarts() {
        //arrange
        setDefaultWatchpoint();
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().times(2);
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(2, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_WatchpointsCached_WhenProgramKilled() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
//        SystemUnderTest.handleEvent(e2);
        setDefaultWatchpoint();

        //assert
        assertEquals(2, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_WatchpointsRemembered_ConsecutiveRuns() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
//        SystemUnderTest.handleEvent(e2);
//        setDefaultWatchpoint();
//        SystemUnderTest.handleEvent(e1);

        //assert
        assertEquals(2, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_WatchpointsRemembered_ConsecutiveRuns_UsingProcessEvent() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ProcessEvent e2 = new ProcessEvent(ProcessEventType.STOPPED);

        //expectation
        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
//        SystemUnderTest.handleEvent(e1);
        SystemUnderTest.handleProcessEvent(e2);
        setDefaultWatchpoint();
//        SystemUnderTest.handleEvent(e1);

        //assert
        assertEquals(2, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_RemoveWatchpoints_InvalidArgument_DoesntThrow() {
        //arrange
        setDefaultWatchpoint();

        //act
        SystemUnderTest.removeWatchpointAction("");

        //assert
        assertEquals(1, SystemUnderTest.getWatchpointsProperty().getValue().size());
    }

    @Test
    public void Test_RemoveWatchpoints_BeforeProgramRuns_NothingSent() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);
        replayAll();

        //act
        SystemUnderTest.removeWatchpointAction(VALUE + ":" + VALUE);
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(0, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void Test_RemoveWatchpoints_TwoEquals_OnlyOneRemoved() {
        //arrange
        setDefaultWatchpoint();
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        _mockEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.removeWatchpointAction(VALUE + ":" + VALUE);
//        SystemUnderTest.handleEvent(e);

        //assert
        assertEquals(1, SystemUnderTest.getWatchpointsProperty().getValue().size());
        verifyAll();
    }
    //endregion

    private void setDefaultBreakpoint() {
        SystemUnderTest.getBreakpointClassProperty().set(VALUE);
        SystemUnderTest.getBreakpointMethodProperty().set(VALUE);
        SystemUnderTest.addBreakpointAction();
    }

    private void setDefaultWatchpoint() {
        SystemUnderTest.getWatchpointClassProperty().set(VALUE);
        SystemUnderTest.getWatchpointFieldProperty().set(VALUE);
        SystemUnderTest.addWatchpointAction();
    }
}
