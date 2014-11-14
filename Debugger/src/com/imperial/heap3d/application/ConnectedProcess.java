package com.imperial.heap3d.application;

import com.sun.jdi.VirtualMachine;

/**
 * Created by oskar on 06/11/14.
 */
public class ConnectedProcess {

    public final VirtualMachine virtualMachine;
    public final Process process;

    public ConnectedProcess(VirtualMachine virtualMachine, Process process) {
        this.process = process;
        this.virtualMachine = virtualMachine;
    }
}
