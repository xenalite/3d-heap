package tests.unit.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.application.ConnectedProcess;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.factories.ProcessFactory;
import com.sun.jdi.VirtualMachine;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oskar on 28/11/14.
 */
public class ProcessFactoryTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ProcessFactory _sut;
    private IVirtualMachineProvider _vmProvider;
    private EventBus _eventBus;

    @Before
    public void setUp() {
        _vmProvider = createMock(IVirtualMachineProvider.class);
        _eventBus = createNiceMock(EventBus.class);
        _sut = new ProcessFactory(_vmProvider, _eventBus, createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_VMProvider_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ProcessFactory(null, createMock(EventBus.class), createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_EventBus_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ProcessFactory(createMock(IVirtualMachineProvider.class),
                null, createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_HeapGraphFactory_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut = new ProcessFactory(createMock(IVirtualMachineProvider.class),
                createMock(EventBus.class), null);
    }

    @Test
    public void callGettersBeforeBuildMethod_DebuggedProcess_Throws() {
        exception.expect(IllegalStateException.class);
        _sut.getDebuggedProcess();
    }

    @Test
    public void callGettersBeforeBuildMethod_ControlEventHandler_Throws() {
        exception.expect(IllegalStateException.class);
        _sut.getControlEventHandler();
    }

    @Test
    public void buildComponents_InvalidArgument_Throws() {
        exception.expect(IllegalArgumentException.class);
        _sut.buildComponents(null);
    }

    @Test
    public void buildComponents_CallsProviders_AsExpected() throws IOException {
        //arrange
        StartDefinition sd = createMock(StartDefinition.class);
        Process p = createMock(Process.class);
        expect(p.getInputStream()).andReturn(createNiceMock(InputStream.class));
        expect(p.getErrorStream()).andReturn(createNiceMock(InputStream.class));

        VirtualMachine vm = createMock(VirtualMachine.class);
        ConnectedProcess cp = createMock(ConnectedProcess.class);
        expect(cp.getProcess()).andReturn(p).times(2);
        expect(cp.getVirtualMachine()).andReturn(vm).times(2);

        expect(_vmProvider.establish(anyInt(), anyObject())).andReturn(cp);
        _eventBus.register(anyObject());
        expectLastCall().once();
        replayAll();

        //act
        _sut.buildComponents(sd);

        //assert

        verifyAll();
    }

    @Test
    public void callGettersBeforeBuildMethod_BreakpointManager_Throws() {
        exception.expect(IllegalStateException.class);
        _sut.getBreakpointManager();
    }

    @Test
    public void callGettersAfterBuildMethod_ReturnsAsExpected() {
        //arrange
        StartDefinition sd = createMock(StartDefinition.class);
        Process p = createMock(Process.class);
        expect(p.getInputStream()).andReturn(createNiceMock(InputStream.class));
        expect(p.getErrorStream()).andReturn(createNiceMock(InputStream.class));

        VirtualMachine vm = createMock(VirtualMachine.class);
        ConnectedProcess cp = createMock(ConnectedProcess.class);
        expect(cp.getProcess()).andReturn(p).times(2);
        expect(cp.getVirtualMachine()).andReturn(vm).times(2);

        expect(_vmProvider.establish(anyInt(), anyObject())).andReturn(cp);
        _eventBus.register(anyObject());
        expectLastCall().once();
        replayAll();

        _sut.buildComponents(sd);

        //act
        Object o1 = _sut.getBreakpointManager();
        Object o2 = _sut.getControlEventHandler();
        Object o3 = _sut.getDebuggedProcess();

        //assert
        assertNotNull(o1);
        assertNotNull(o2);
        assertNotNull(o3);
    }
}