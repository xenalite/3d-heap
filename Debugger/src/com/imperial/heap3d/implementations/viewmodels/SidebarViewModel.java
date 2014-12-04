package com.imperial.heap3d.implementations.viewmodels;

import com.imperial.heap3d.interfaces.viewmodels.*;

/**
 * Created by om612 on 04/12/14.
 */
public class SidebarViewModel implements ISidebarViewModel {

    private IApplicationTabViewModel _applicationTabViewModel;
    private IBreakpointsTabViewModel _breakpointsTabViewModel;
    private IHeapInfoTabViewModel _heapInfoTabViewModel;

    public SidebarViewModel(IApplicationTabViewModel applicationTabViewModel,
                             IBreakpointsTabViewModel breakpointsTabViewModel,
                             IHeapInfoTabViewModel heapInfoTabViewModel) {

        _applicationTabViewModel = applicationTabViewModel;
        _breakpointsTabViewModel = breakpointsTabViewModel;
        _heapInfoTabViewModel = heapInfoTabViewModel;
    }




}
