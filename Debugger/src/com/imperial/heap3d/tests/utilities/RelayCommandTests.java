package com.imperial.heap3d.tests.utilities;

import com.imperial.heap3d.utilities.RelayCommand;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by om612 on 19/11/14.
 */
public class RelayCommandTests extends EasyMockSupport {

    private RelayCommand _sut;
    private Runnable mRunnable;

    @Before
    public void setUp() {
        mRunnable = createMock(Runnable.class);
        _sut = new RelayCommand(mRunnable);
    }

    @After
    public void tearDown() {
        verifyAll();
    }

    @Test
    public void constructor_WithInvalidArguments_Throws() {
        replayAll();
        try {
            _sut = new RelayCommand(null);
        }
        catch(IllegalArgumentException ignored) {}
    }

    @Test
    public void disabled_ByDefault() {
        replayAll();
        Assert.assertFalse(_sut.canExecute().get());
    }

    @Test
    public void canExecute_RunnableCalled() {
        //arrange
        mRunnable.run();
        replayAll();

        //act
        _sut.execute();
    }

    @Test
    public void cannotExecute_RunnableNotCalled() {
        //arrange
        _sut.canExecute().set(false);
        replayAll();

        //act
        _sut.execute();
    }
}
