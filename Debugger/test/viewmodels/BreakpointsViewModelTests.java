package com.imperial.heap3d.tests.viewmodels;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.ControlEvent;
import com.imperial.heap3d.events.EventType;
import com.imperial.heap3d.events.ProcessEvent;
import com.imperial.heap3d.events.ProcessEventType;
import com.imperial.heap3d.ui.viewmodels.BreakpointsViewModel;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointsViewModelTests extends EasyMockSupport {

    private BreakpointsViewModel _sut;
    private EventBus mEventBus;
    private static final String VALUE = "VALUE";

    @Before
    public void setUp() {
        mEventBus = createMock(EventBus.class);
        mEventBus.register(anyObject());
        expectLastCall();
        replayAll();
        _sut = new BreakpointsViewModel(mEventBus);
        verifyAll();
        resetAllToDefault();
    }

    @Test
    public void constructor_WithInvalidArguments_Throws() {
        try {
            _sut = new BreakpointsViewModel(null);
        }
        catch(IllegalArgumentException ignored) {}
    }

    @Test
    public void initially_NoBreakpointsDefined() {
        assertTrue(_sut.getWatchpointsProperty().getValue().isEmpty());
        assertTrue(_sut.getBreakpointsProperty().getValue().isEmpty());
        assertTrue(_sut.getBreakpointClassProperty().getValue().isEmpty());
        assertTrue(_sut.getWatchpointClassProperty().getValue().isEmpty());
        assertTrue(_sut.getWatchpointFieldProperty().getValue().isEmpty());
        assertTrue(_sut.getBreakpointMethodProperty().getValue().isEmpty());
    }

    @Test
    public void addBreakpoint_noClassNameOrMethod_NotAdded() {
        //act
        _sut.addBreakpointAction();

        //assert
        assertTrue(_sut.getBreakpointsProperty().getValue().isEmpty());
        assertEquals("", _sut.getBreakpointClassProperty().get());
        assertEquals("", _sut.getBreakpointMethodProperty().get());
    }

    @Test
    public void addBreakpoint_noMethodName_NotAdded() {
        //arrange
        _sut.getBreakpointClassProperty().set(VALUE);
        //act
        _sut.addBreakpointAction();

        //assert
        assertTrue(_sut.getBreakpointsProperty().getValue().isEmpty());
        assertEquals(VALUE, _sut.getBreakpointClassProperty().get());
        assertEquals("", _sut.getBreakpointMethodProperty().get());
    }

    @Test
    public void addBreakpoint_noClassName_NotAdded() {
        //arrange
        _sut.getBreakpointMethodProperty().set(VALUE);
        //act
        _sut.addBreakpointAction();

        //assert
        assertTrue(_sut.getBreakpointsProperty().getValue().isEmpty());
        assertEquals("", _sut.getBreakpointClassProperty().get());
        assertEquals(VALUE, _sut.getBreakpointMethodProperty().get());
    }

    @Test
    public void addBreakpoint_validArguments_Added() {
        //arrange
        _sut.getBreakpointClassProperty().set(VALUE);
        _sut.getBreakpointMethodProperty().set(VALUE);

        //act
        _sut.addBreakpointAction();

        //assert
        assertEquals(1, _sut.getBreakpointsProperty().getValue().size());
        assertEquals("", _sut.getBreakpointClassProperty().get());
        assertEquals("", _sut.getBreakpointMethodProperty().get());
    }

    @Test
    public void addWatchpoint_noClassNameOrMethod_NotAdded() {
        //act
        _sut.addWatchpointAction();

        //assert
        assertTrue(_sut.getWatchpointsProperty().getValue().isEmpty());
        assertEquals("", _sut.getWatchpointClassProperty().get());
        assertEquals("", _sut.getWatchpointFieldProperty().get());
    }

    @Test
    public void addWatchpoint_noMethodName_NotAdded() {
        //arrange
        _sut.getWatchpointClassProperty().set(VALUE);
        //act
        _sut.addWatchpointAction();

        //assert
        assertTrue(_sut.getWatchpointsProperty().getValue().isEmpty());
        assertEquals(VALUE, _sut.getWatchpointClassProperty().get());
        assertEquals("", _sut.getWatchpointFieldProperty().get());
    }

    @Test
    public void addWatchpoint_noClassName_NotAdded() {
        //arrange
        _sut.getWatchpointFieldProperty().set(VALUE);
        //act
        _sut.addWatchpointAction();

        //assert
        assertTrue(_sut.getWatchpointsProperty().getValue().isEmpty());
        assertEquals("", _sut.getWatchpointClassProperty().get());
        assertEquals(VALUE, _sut.getWatchpointFieldProperty().get());
    }

    @Test
    public void addWatchpoint_validArguments_Added() {
        //arrange
        _sut.getWatchpointClassProperty().set(VALUE);
        _sut.getWatchpointFieldProperty().set(VALUE);

        //act
        _sut.addWatchpointAction();

        //assert
        assertEquals(1, _sut.getWatchpointsProperty().getValue().size());
        assertEquals("", _sut.getWatchpointClassProperty().get());
        assertEquals("", _sut.getWatchpointFieldProperty().get());
    }

    @Test
    public void watchpointsCached_ByDefault() {
        //expect -- no posts to event bus
        replayAll();
        //arrange
        _sut.getWatchpointFieldProperty().set(VALUE);
        _sut.getWatchpointClassProperty().set(VALUE);

        //act
        _sut.addWatchpointAction();

        //assert
        verifyAll();
    }

    @Test
    public void breakpointsCached_ByDefault() {
        //expect -- no posts to event bus
        replayAll();
        //act
        setDefaultBreakpoint();

        //assert
        verifyAll();
    }

    private void setDefaultBreakpoint() {
        _sut.getBreakpointClassProperty().set(VALUE);
        _sut.getBreakpointMethodProperty().set(VALUE);
        _sut.addBreakpointAction();
    }

    private void setDefaultWatchpoint() {
        _sut.getWatchpointClassProperty().set(VALUE);
        _sut.getWatchpointFieldProperty().set(VALUE);
        _sut.addWatchpointAction();
    }

    @Test
    public void breakpointsSent_WhenProgramStarts() {
        //arrange
        setDefaultBreakpoint();
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().times(2);
        replayAll();

        //act
        _sut.handleEvent(e);

        //assert
        assertEquals(2, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void breakpointsCached_WhenProgramKilled() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleEvent(e2);
        setDefaultBreakpoint();

        //assert
        assertEquals(2, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void breakpointsRemembered_ConsecutiveRuns() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleEvent(e2);
        setDefaultBreakpoint();
        _sut.handleEvent(e1);

        //assert
        assertEquals(2, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void breakpointsRemembered_ConsecutiveRuns_UsingProcessEvent() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ProcessEvent e2 = new ProcessEvent(ProcessEventType.STOPPED);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleProcessEvent(e2);
        setDefaultBreakpoint();
        _sut.handleEvent(e1);

        //assert
        assertEquals(2, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void removeBreakpoints_InvalidArgument_DoesntThrow() {
        //arrange
        setDefaultBreakpoint();

        //act
        _sut.removeBreakpointAction("");

        //assert
        assertEquals(1, _sut.getBreakpointsProperty().getValue().size());
    }

    @Test
    public void removeBreakpoints_BeforeProgramRuns_NothingSent() {
        //arrange
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);
        replayAll();

        //act
        _sut.removeBreakpointAction(VALUE + ":" + VALUE);
        _sut.handleEvent(e);

        //assert
        assertEquals(0, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void removeBreakpoints_TwoEquals_OnlyOneRemoved() {
        //arrange
        setDefaultBreakpoint();
        setDefaultBreakpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().once();
        replayAll();

        //act
        _sut.removeBreakpointAction(VALUE + ":" + VALUE);
        _sut.handleEvent(e);

        //assert
        assertEquals(1, _sut.getBreakpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void watchpointsSent_WhenProgramStarts() {
        //arrange
        setDefaultWatchpoint();
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().times(2);
        replayAll();

        //act
        _sut.handleEvent(e);

        //assert
        assertEquals(2, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void watchpointsCached_WhenProgramKilled() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleEvent(e2);
        setDefaultWatchpoint();

        //assert
        assertEquals(2, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void watchpointsRemembered_ConsecutiveRuns() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ControlEvent e2 = new ControlEvent(EventType.STOP, null, null);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleEvent(e2);
        setDefaultWatchpoint();
        _sut.handleEvent(e1);

        //assert
        assertEquals(2, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void watchpointsRemembered_ConsecutiveRuns_UsingProcessEvent() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e1 = new ControlEvent(EventType.START, null, null);
        ProcessEvent e2 = new ProcessEvent(ProcessEventType.STOPPED);

        //expectation
        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().atLeastOnce();
        replayAll();

        //act
        _sut.handleEvent(e1);
        _sut.handleProcessEvent(e2);
        setDefaultWatchpoint();
        _sut.handleEvent(e1);

        //assert
        assertEquals(2, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void removeWatchpoints_InvalidArgument_DoesntThrow() {
        //arrange
        setDefaultWatchpoint();

        //act
        _sut.removeWatchpointAction("");

        //assert
        assertEquals(1, _sut.getWatchpointsProperty().getValue().size());
    }

    @Test
    public void removeWatchpoints_BeforeProgramRuns_NothingSent() {
        //arrange
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);
        replayAll();

        //act
        _sut.removeWatchpointAction(VALUE + ":" + VALUE);
        _sut.handleEvent(e);

        //assert
        assertEquals(0, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }

    @Test
    public void removeWatchpoints_TwoEquals_OnlyOneRemoved() {
        //arrange
        setDefaultWatchpoint();
        setDefaultWatchpoint();
        ControlEvent e = new ControlEvent(EventType.START, null, null);

        mEventBus.post(anyObject(ControlEvent.class));
        expectLastCall().once();
        replayAll();

        //act
        _sut.removeWatchpointAction(VALUE + ":" + VALUE);
        _sut.handleEvent(e);

        //assert
        assertEquals(1, _sut.getWatchpointsProperty().getValue().size());
        verifyAll();
    }
}
