package tests.unit.utilities;

import com.imperial.heap3d.utilities.RelayCommand;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by om612 on 19/11/14.
 */
public class RelayCommandTests extends EasyMockSupport {

    private RelayCommand SystemUnderTest;
    private Runnable _mockRunnable;

    @Before
    public void SetUp() {
        _mockRunnable = createMock(Runnable.class);
        SystemUnderTest = new RelayCommand(_mockRunnable);
    }

    @Test
    public void Test_Constructor_WithInvalidArguments_Throws() {
        try {
            SystemUnderTest = new RelayCommand(null);
        }
        catch(IllegalArgumentException ignored) {}
    }

    @Test
    public void Test_Disabled_ByDefault() {
        Assert.assertFalse(SystemUnderTest.canExecute().get());
    }

    @Test
    public void Test_CanExecute_RunnableCalled() {
        //arrange
        SystemUnderTest.canExecute().set(true);
        _mockRunnable.run();
        replayAll();

        //act
        SystemUnderTest.execute();

        //assert
        verifyAll();
    }

    @Test
    public void cannotExecute_RunnableNotCalled() {
        //arrange
        SystemUnderTest.canExecute().set(false);
        replayAll();

        //act
        SystemUnderTest.execute();

        //assert
        verifyAll();
    }
}
