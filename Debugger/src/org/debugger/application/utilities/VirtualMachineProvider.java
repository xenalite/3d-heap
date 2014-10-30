package org.debugger.application.utilities;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.ListeningConnector;

import java.io.IOException;
import java.util.Map;

/**
 * Created by oskar on 29/10/14.
 */
public class VirtualMachineProvider implements IVirtualMachineProvider {

    private VirtualMachine _instance;

    @Override
    public VirtualMachine getVirtualMachine() {
        return _instance;
    }

    @Override
    public void createAtPort(int port) {
        try {
            _instance = connect(getConnector(), Integer.toString(port));
        }
        catch(IOException | IllegalConnectorArgumentsException e) {
            throw new IllegalStateException(e);
        }
    }

    private ListeningConnector getConnector() {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        return vmManager.listeningConnectors().get(0);
    }

    private VirtualMachine connect(ListeningConnector connector, String port)
            throws IllegalConnectorArgumentsException, IOException {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        args.get("port").setValue(port);

        connector.startListening(args);
        return connector.accept(args);
    }
}