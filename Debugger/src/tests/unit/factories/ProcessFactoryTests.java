package tests.unit.factories;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.events.StartDefinition;
import com.imperial.heap3d.factories.HeapGraphFactory;
import com.imperial.heap3d.factories.IVirtualMachineProvider;
import com.imperial.heap3d.factories.ProcessFactory;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oskar on 28/11/14.
 */
public class ProcessFactoryTests extends EasyMockSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ProcessFactory _sut;

    @Before
    public void setUp() {
        _sut = new ProcessFactory(createMock(IVirtualMachineProvider.class),
                createMock(EventBus.class), createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_VMProvider_Throws() {
        exception.expect(IllegalStateException.class);
        _sut = new ProcessFactory(null, createMock(EventBus.class), createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_EventBus_Throws() {
        exception.expect(IllegalStateException.class);
        _sut = new ProcessFactory(createMock(IVirtualMachineProvider.class),
                null, createMock(HeapGraphFactory.class));
    }

    @Test
    public void constructor_InvalidArgument_HeapGraphFactory_Throws() {
        exception.expect(IllegalStateException.class);
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
        exception.expect(IllegalStateException.class);
        _sut.buildComponents(null);
    }

    @Test
    public void buildComponents_CallsProviders_AsExpected() throws IOException {
        //arrange
        StartDefinition sd = createMock(StartDefinition.class);
        Process p = createMock(Process.class);
        expect(sd.buildProcess(anyInt())).andReturn(p);

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
    public void callGettersAfterBuildMethod_DoesNotReturnNulls() {
        //arrange
        _sut.buildComponents(createMock(StartDefinition.class));

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
