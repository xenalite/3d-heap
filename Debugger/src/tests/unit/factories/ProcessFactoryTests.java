package tests.unit.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.implementations.application.ConnectedProcess;
import com.imperial.heap3d.implementations.events.StartDefinition;
import com.imperial.heap3d.implementations.factories.HeapGraphFactory;
import com.imperial.heap3d.implementations.factories.ProcessFactory;
import com.imperial.heap3d.interfaces.factories.IVirtualMachineProvider;
import com.sun.jdi.VirtualMachine;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;

import static org.easymock.EasyMock.*;

/**
 * Created by oskar on 28/11/14.
 */
public class ProcessFactoryTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ProcessFactory SystemUnderTest;
    private IVirtualMachineProvider _mockProvider;
    private EventBus _mockEventbus;
    private HeapGraphFactory _mockHeapGraphFactory;

    @Before
    public void SetUp() {
        _mockProvider = createMock(IVirtualMachineProvider.class);
        _mockEventbus = createMock(EventBus.class);
        _mockHeapGraphFactory = createMock(HeapGraphFactory.class);
        SystemUnderTest = new ProcessFactory(_mockProvider, _mockEventbus, _mockHeapGraphFactory);
        resetAll();
    }

    @Test
    public void Test_Constructor_InvalidArgument_VMProvider_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new ProcessFactory(null, _mockEventbus, _mockHeapGraphFactory);
    }

    @Test
    public void Test_Constructor_InvalidArgument_EventBus_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new ProcessFactory(_mockProvider, null, _mockHeapGraphFactory);
    }

    @Test
    public void Test_Constructor_InvalidArgument_HeapGraphFactory_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest = new ProcessFactory(_mockProvider, _mockEventbus, null);
    }

    @Test
    public void Test_BuildComponents_InvalidArgument_Throws() {
        exception.expect(IllegalArgumentException.class);
        SystemUnderTest.buildComponents(null);
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

        expect(_mockProvider.establish(anyInt(), anyObject())).andReturn(cp);
        _mockEventbus.register(anyObject());
        expectLastCall().once();
        replayAll();

        //act
        SystemUnderTest.buildComponents(sd);

        //assert

        verifyAll();
    }

    @Test
    public void callGettersBeforeBuildMethod_BreakpointManager_Throws() {
        exception.expect(IllegalStateException.class);
//        SystemUnderTest.getBreakpointManager();
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

        expect(_mockProvider.establish(anyInt(), anyObject())).andReturn(cp);
        _mockEventbus.register(anyObject());
        expectLastCall().once();
        replayAll();

        SystemUnderTest.buildComponents(sd);

        //act
//        Object o1 = SystemUnderTest.getBreakpointManager();
//        Object o2 = SystemUnderTest.getControlEventHandler();
//        Object o3 = SystemUnderTest.getDebuggedProcess();

        //assert
//        assertNotNull(o1);
//        assertNotNull(o2);
//        assertNotNull(o3);
    }
}