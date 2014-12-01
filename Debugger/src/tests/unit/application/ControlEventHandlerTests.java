package tests.unit.application;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.application.BreakpointManager;
import com.imperial.heap3d.implementations.application.ControlEventHandler;
import com.imperial.heap3d.implementations.application.DebuggedProcess;
import com.imperial.heap3d.implementations.events.ControlEvent;
import com.imperial.heap3d.implementations.events.EventType;
import com.imperial.heap3d.implementations.events.ProcessEvent;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.expectLastCall;

/**
 * Created by oskar on 29/11/14.
 */
public class ControlEventHandlerTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ControlEventHandler _sut;

    private DebuggedProcess _mockDebuggedProcess;
    private EventBus _mockEventBus;
    private BreakpointManager _mockBreakpointManager;

    private final String CLASSNAME = "CLASS";
    private final String ARGUMENT = "ARGUMENT";

    @Before
    public void setUp() {
        _mockDebuggedProcess = createNiceMock(DebuggedProcess.class);
        _mockEventBus = createNiceMock(EventBus.class);
        _mockBreakpointManager = createNiceMock(BreakpointManager.class);
        _sut = new ControlEventHandler(_mockDebuggedProcess, _mockEventBus, _mockBreakpointManager);
        resetAll();
    }

    @Test
    public void Constructor_InvalidArgument_DebuggedProcess_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ControlEventHandler(null, _mockEventBus, _mockBreakpointManager);
    }

    @Test
    public void Constructor_InvalidArgument_EventBus_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ControlEventHandler(_mockDebuggedProcess, null, _mockBreakpointManager);
    }

    @Test
    public void Constructor_InvalidArgument_BreakpointManager_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ControlEventHandler(_mockDebuggedProcess, _mockEventBus, null);
    }

    @Test
    public void Constructor_InitialisesEventBus_AndSendsStart() {
        //arrange
        _mockEventBus.register(anyObject(ControlEventHandler.class));
        expectLastCall().once();
        _mockEventBus.post(anyObject(ProcessEvent.class));
        expectLastCall().once();

        replayAll();

        //act
        _sut = new ControlEventHandler(_mockDebuggedProcess, _mockEventBus, _mockBreakpointManager);

        //assert
        verifyAll();
    }

    @Test
    public void Dispose_UnsubscribesEventBus_AndDisposesProcess_OnStopEvent() throws InterruptedException {
        //arrange
        _mockEventBus.unregister(_sut);
        expectLastCall().once();
        _mockDebuggedProcess.dispose();
        expectLastCall().once();

        replayAll();
        //act
        _sut.handleEvent(new ControlEvent(EventType.STOP, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Dispose_UnsubscribesEventBus_AndDisposesProcess_OnVMDeathEvent() throws InterruptedException {
        //arrange
        _mockEventBus.unregister(_sut);
        expectLastCall().once();
        expect(_mockDebuggedProcess.handleEvents()).andReturn(false);
        _mockDebuggedProcess.dispose();
        expectLastCall().once();

        replayAll();
        //act
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_PauseEvent() throws InterruptedException {
        //arrange
        
        _mockDebuggedProcess.pause();
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.PAUSE, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_ResumeEvent() throws InterruptedException {
        //arrange
        
        _mockDebuggedProcess.resume();
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.RESUME, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_StepOverEvent() throws InterruptedException {
        //arrange
        
        _mockDebuggedProcess.createStepOverRequest();
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.STEPOVER, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_StepIntoEvent() throws InterruptedException {
        //arrange
        
        _mockDebuggedProcess.createStepIntoRequest();
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.STEPINTO, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_StepOutEvent() throws InterruptedException {
        //arrange
        
        _mockDebuggedProcess.createStepOutRequest();
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.STEPOUT, null, null));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_BreakpointEvent() throws InterruptedException {
        //arrange
        
        _mockBreakpointManager.addBreakpoint(CLASSNAME, ARGUMENT);
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.BREAKPOINT, CLASSNAME, ARGUMENT));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_WatchpointEvent() throws InterruptedException {
        //arrange
        
        _mockBreakpointManager.addWatchpoint(CLASSNAME, ARGUMENT);
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.WATCHPOINT, CLASSNAME, ARGUMENT));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_RemoveBreakpointEvent() throws InterruptedException {
        //arrange
        
        _mockBreakpointManager.removeBreakpoint(CLASSNAME, ARGUMENT);
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.RMBREAKPOINT, CLASSNAME, ARGUMENT));
        _sut.run();

        //assert
        verifyAll();
    }

    @Test
    public void Handles_RemoveWatchpointEvent() throws InterruptedException {
        //arrange
        
        _mockBreakpointManager.removeWatchpoint(CLASSNAME, ARGUMENT);
        expectLastCall().once();

        replayAll();

        //act
        _sut.handleEvent(new ControlEvent(EventType.RMWATCHPOINT, CLASSNAME, ARGUMENT));
        _sut.run();

        //assert
        verifyAll();
    }

}
