package com.imperial.heap3d.factories;

import com.imperial.heap3d.application.ConnectedProcess;
import com.sun.jdi.VirtualMachine;

import java.util.concurrent.Callable;

/**
 * Created by oskar on 29/10/14.
 */
public interface IVirtualMachineProvider {

    public VirtualMachine connect(int port);

    public ConnectedProcess establish(int port, Callable<Process> p);
}
