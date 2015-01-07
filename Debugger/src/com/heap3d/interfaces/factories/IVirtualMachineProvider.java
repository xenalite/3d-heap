package com.heap3d.interfaces.factories;

import com.heap3d.implementations.application.ConnectedProcess;
import com.sun.jdi.VirtualMachine;

import java.util.concurrent.Callable;

/**
 * Created by oskar on 29/10/14.
 */
public interface IVirtualMachineProvider {

    public VirtualMachine connect(int port);

    public ConnectedProcess establish(int port, Callable<Process> p);
}
