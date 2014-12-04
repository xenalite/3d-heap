package com.imperial.heap3d.implementations.viewmodels;

import com.imperial.heap3d.interfaces.viewmodels.IApplicationTabViewModel;
import com.imperial.heap3d.interfaces.viewmodels.IBreakpointsTabViewModel;
import com.imperial.heap3d.interfaces.viewmodels.IHeapInfoTabViewModel;
import com.imperial.heap3d.interfaces.viewmodels.IMainViewModel;

/**
 * Created by om612 on 04/12/14.
 */
public class MainViewModel implements IMainViewModel {

    private IApplicationTabViewModel _applicationTabViewModel;
    private IBreakpointsTabViewModel _breakpointsTabViewModel;
    private IHeapInfoTabViewModel _heapInfoTabViewModel;

    public MainViewModel(IApplicationTabViewModel applicationTabViewModel,
                         IBreakpointsTabViewModel breakpointsTabViewModel,
                         IHeapInfoTabViewModel heapInfoTabViewModel) {

        _applicationTabViewModel = applicationTabViewModel;
        _breakpointsTabViewModel = breakpointsTabViewModel;
        _heapInfoTabViewModel = heapInfoTabViewModel;
    }




}
