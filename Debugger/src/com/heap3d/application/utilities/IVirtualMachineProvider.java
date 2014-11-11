package com.heap3d.application.utilities;

import com.sun.jdi.VirtualMachine;

import java.util.concurrent.Callable;

/**
 * Created by oskar on 29/10/14.
 */
public interface IVirtualMachineProvider {

    public VirtualMachine connect(int port);

    public ConnectedProcess establish(int port, Callable<Process> p);
}
