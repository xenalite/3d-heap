package org.debugger.application.utilities;

import com.sun.jdi.VirtualMachine;

/**
 * Created by oskar on 29/10/14.
 */
public interface IVirtualMachineProvider {

    public VirtualMachine getVirtualMachine();

    public void createAtPort(int port);
}
