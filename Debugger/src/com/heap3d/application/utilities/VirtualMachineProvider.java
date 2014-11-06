package com.heap3d.application.utilities;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by oskar on 29/10/14.
 */
public class VirtualMachineProvider implements IVirtualMachineProvider {

    @Override
    public VirtualMachine connect(int port) {
        try {
            return connect(getConnector(), Integer.toString(port));
        }
        catch(IOException | IllegalConnectorArgumentsException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private AttachingConnector getConnector() {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        return vmManager.attachingConnectors().get(0);
    }

    private VirtualMachine connect(AttachingConnector connector, String port)
            throws IllegalConnectorArgumentsException, IOException {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        args.get("port").setValue(port);

        return connector.attach(args);
    }
}