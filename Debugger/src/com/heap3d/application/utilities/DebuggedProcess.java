package com.heap3d.application.utilities;

import com.sun.jdi.VirtualMachine;

/**
 * Created by oskar on 06/11/14.
 */
public class DebuggedProcess {

    public final VirtualMachine virtualMachine;
    public final Process process;

    public DebuggedProcess(VirtualMachine virtualMachine, Process process) {
        this.process = process;
        this.virtualMachine = virtualMachine;
    }
}