package tests.unit.application;

import com.imperial.heap3d.implementations.application.BreakpointManager;
import com.sun.jdi.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.LinkedList;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.powermock.api.easymock.PowerMock.expectLastCall;

/**
 * Created by oskar on 29/11/14.
 */
public class BreakpointManagerTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private BreakpointManager _sut;
    private VirtualMachine _mockInstance;

    private final String CLASSNAME = "CLASS";
    private final String ARGUMENT = "ARGUMENT";

    @Before
    public void setUp() {
        _mockInstance = createMock(VirtualMachine.class);
        _sut = new BreakpointManager(_mockInstance);
    }

    @Test
    public void Constructor_InvalidArguments_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new BreakpointManager(null);
    }

    @Test
    public void AddBreakpoint_InvalidArguments_FailSilently() {
        _sut.addBreakpoint(CLASSNAME, null);
        _sut.addBreakpoint(null, ARGUMENT);
    }

    @Test
    public void AddWatchpoint_InvalidArguments_FailSilently() {
        _sut.addWatchpoint(CLASSNAME, null);
        _sut.addWatchpoint(null, ARGUMENT);
    }

    @Test
    public void ClassNotLoaded_AddBreakpoint_CreatesClassPrepareRequest_OnlyOnce() {
        //arrange
        createExpectations_ClassNotLoaded();
        replayAll();

        //act
        _sut.addBreakpoint(CLASSNAME, ARGUMENT);
        _sut.addBreakpoint(CLASSNAME, ARGUMENT+1);

        //assert
        verifyAll();
    }

    @Test
    public void ClassNotLoaded_AddWatchpoint_CreatesClassPrepareRequest_OnlyOnce() {
        //arrange
        createExpectations_ClassNotLoaded();
        replayAll();

        //act
        _sut.addWatchpoint(CLASSNAME, ARGUMENT);
        _sut.addWatchpoint(CLASSNAME, ARGUMENT+1);

        //assert
        verifyAll();
    }

    private void createExpectations_ClassNotLoaded() {
        expect(_mockInstance.classesByName(CLASSNAME))
                .andReturn(new LinkedList<>()).times(2);

        EventRequestManager erm = createMock(EventRequestManager.class);
        ClassPrepareRequest cpr = createMock(ClassPrepareRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm);
        expect(erm.createClassPrepareRequest()).andReturn(cpr);
        cpr.addClassFilter(CLASSNAME);
        expectLastCall().once();
        cpr.enable();
        expectLastCall().once();
    }

    @Test
    public void ClassesNotLoaded_Multiple_AddBreakpoint_CreatesClassPrepareRequest_Ntimes() {
        //arrange
        createExpectations_ClassNotLoaded_Multiple();
        replayAll();

        //act
        _sut.addBreakpoint(CLASSNAME+1, ARGUMENT);
        _sut.addBreakpoint(CLASSNAME+2, ARGUMENT);
        _sut.addBreakpoint(CLASSNAME+3, ARGUMENT);

        //assert
        verifyAll();
    }

    @Test
    public void ClassNotLoaded_Multiple_AddWatchpoint_CreatesClassPrepareRequest_Ntimes() {
        //arrange
        createExpectations_ClassNotLoaded_Multiple();
        replayAll();

        //act
        _sut.addWatchpoint(CLASSNAME+1, ARGUMENT);
        _sut.addWatchpoint(CLASSNAME+2, ARGUMENT);
        _sut.addWatchpoint(CLASSNAME+3, ARGUMENT);

        //assert
        verifyAll();
    }

    private void createExpectations_ClassNotLoaded_Multiple() {
        expect(_mockInstance.classesByName(CLASSNAME+1)).andReturn(new LinkedList<>());
        expect(_mockInstance.classesByName(CLASSNAME+2)).andReturn(new LinkedList<>());
        expect(_mockInstance.classesByName(CLASSNAME+3)).andReturn(new LinkedList<>());

        EventRequestManager erm = createMock(EventRequestManager.class);
        ClassPrepareRequest cpr = createMock(ClassPrepareRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm).times(3);
        expect(erm.createClassPrepareRequest()).andReturn(cpr).times(3);
        cpr.addClassFilter(anyString());
        expectLastCall().times(3);
        cpr.enable();
        expectLastCall().times(3);
    }

    @Test
    public void ClassLoaded_AddBreakpoint_CreatesBreakpointRequest() {
        //arrange
        ReferenceType rt = createMock(ReferenceType.class);
        expect(_mockInstance.classesByName(CLASSNAME))
                .andReturn(new LinkedList<ReferenceType>() {{
                    add(rt);
                }});

        EventRequestManager erm = createMock(EventRequestManager.class);
        Location l = createMock(Location.class);
        Method m = createMock(Method.class);
        BreakpointRequest br = createMock(BreakpointRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm);
        expect(rt.methodsByName(ARGUMENT)).andReturn(new LinkedList<Method>() {{ add(m); }});
        expect(m.location()).andReturn(l);
        expect(erm.createBreakpointRequest(l)).andReturn(br);
        br.enable();
        expectLastCall().once();
        replayAll();

        //act
        _sut.addBreakpoint(CLASSNAME, ARGUMENT);

        //assert
        verifyAll();
    }

    @Test
    public void ClassLoaded_AddWatchpoint_CreatesWatchpointRequest() {
        //arrange
        ReferenceType rt = createMock(ReferenceType.class);
        expect(_mockInstance.classesByName(CLASSNAME))
                .andReturn(new LinkedList<ReferenceType>() {{ add(rt); }});

        EventRequestManager erm = createMock(EventRequestManager.class);
        Field f = createMock(Field.class);
        ModificationWatchpointRequest mwr = createMock(ModificationWatchpointRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm);
        expect(rt.fieldByName(ARGUMENT)).andReturn(f);
        expect(erm.createModificationWatchpointRequest(f)).andReturn(mwr);
        mwr.enable();
        expectLastCall().once();
        replayAll();

        //act
        _sut.addWatchpoint(CLASSNAME, ARGUMENT);

        //assert
        verifyAll();
    }

    @Ignore
    @Test
    public void ClassLoaded_RemoveBreakpoint_BreakpointRequestRemoved() {
        //arrange
        ReferenceType rt = createMock(ReferenceType.class);
        expect(_mockInstance.classesByName(CLASSNAME))
                .andReturn(new LinkedList<ReferenceType>() {{ add(rt); }});

        EventRequestManager erm = createMock(EventRequestManager.class);
        BreakpointRequest br = createMock(BreakpointRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm);
        expect(erm.breakpointRequests().stream().filter(anyObject())
                .findFirst()).andReturn(Optional.of(br));
        erm.deleteEventRequest(br);
        expectLastCall().once();
        replayAll();

        //act
        _sut.removeBreakpoint(CLASSNAME, ARGUMENT);

        //assert
        verifyAll();
    }

    @Ignore
    @Test
    public void ClassLoaded_RemoveWatchpoint_WatchpointRequestRemoved() {
        //arrange
        ReferenceType rt = createMock(ReferenceType.class);
        expect(_mockInstance.classesByName(CLASSNAME))
                .andReturn(new LinkedList<ReferenceType>() {{ add(rt); }});

        EventRequestManager erm = createMock(EventRequestManager.class);
        ModificationWatchpointRequest mwr = createMock(ModificationWatchpointRequest.class);
        expect(_mockInstance.eventRequestManager()).andReturn(erm);
        expect(erm.modificationWatchpointRequests())
                .andReturn(new LinkedList<ModificationWatchpointRequest>() {{ add(mwr); }});
        erm.deleteEventRequest(mwr);
        expectLastCall().once();
        replayAll();

        //act
        _sut.removeWatchpoint(CLASSNAME, ARGUMENT);

        //assert
        verifyAll();
    }

    @Test
    public void RemoveBreakpoint_InvalidArguments_FailSilently() {
        _sut.removeBreakpoint(null, ARGUMENT);
        _sut.removeBreakpoint(CLASSNAME, null);
    }

    @Test
    public void RemoveWatchpoint_InvalidArguments_FailSilently() {
        _sut.removeWatchpoint(CLASSNAME, null);
        _sut.removeBreakpoint(null, ARGUMENT);
    }

    @Test
    public void NotifyClassLoaded_MultipleRequestsAdded() {
        //arrange
        expect(_mockInstance.classesByName(anyString()))
                .andReturn(new LinkedList<>()).times(3);

        EventRequestManager erm = createMock(EventRequestManager.class);
        ClassPrepareRequest cpr = createMock(ClassPrepareRequest.class);
        ReferenceType rt = createMock(ReferenceType.class);
        Field f = createMock(Field.class);
        Location l = createMock(Location.class);
        Method m = createMock(Method.class);
        BreakpointRequest br = createMock(BreakpointRequest.class);
        ModificationWatchpointRequest mwr = createMock(ModificationWatchpointRequest.class);

        expect(_mockInstance.eventRequestManager()).andReturn(erm).times(4);
        expect(erm.createClassPrepareRequest()).andReturn(cpr).times(2);
        cpr.addClassFilter(anyString());
        expectLastCall().times(2);
        cpr.enable();
        expectLastCall().times(2);
        expect(rt.name()).andReturn(CLASSNAME);
        expect(rt.methodsByName(ARGUMENT)).andReturn(new LinkedList<Method>() {{
            add(m);
        }});
        expect(m.location()).andReturn(l);
        expect(erm.createBreakpointRequest(l)).andReturn(br);
        br.enable();
        expectLastCall().once();
        expect(rt.fieldByName(ARGUMENT)).andReturn(f);
        expect(erm.createModificationWatchpointRequest(f)).andReturn(mwr);
        mwr.enable();
        expectLastCall().once();
        replayAll();

        //act
        _sut.addBreakpoint(CLASSNAME, ARGUMENT);
        _sut.addBreakpoint(CLASSNAME+1, ARGUMENT);
        _sut.addWatchpoint(CLASSNAME, ARGUMENT);

        _sut.notifyClassLoaded(rt);

        //assert
        verifyAll();
    }
}