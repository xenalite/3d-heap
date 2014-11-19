package com.imperial.heap3d.tests.viewmodels;

import com.google.common.eventbus.EventBus;
import com.imperial.heap3d.ui.viewmodels.BreakpointsViewModel;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointsViewModelTests extends EasyMockSupport {

    private BreakpointsViewModel _sut;
    private EventBus mEventBus;

    @Before
    public void setUp() {
        mEventBus = createMock(EventBus.class);
        _sut = new BreakpointsViewModel(mEventBus);
    }

    @After
    public void tearDown() {
        verifyAll();
    }

    @Test
    public void constructor_WithInvalidArguments_Throws() {
        replayAll();
        try {
            _sut = new BreakpointsViewModel(null);
        }
        catch(IllegalArgumentException ignored) {}
    }

    @Test
    public void initially_NoBreakpointsDefined() {
        //arrange
        replayAll();

        //act
        //assert
        assertTrue(_sut.getWatchpointsProperty().getValue().isEmpty());
        assertTrue(_sut.getBreakpointsProperty().getValue().isEmpty());
    }
}
