package com.heap3d.implementations.factories;

import com.heap3d.implementations.application.ConnectedProcess;
import com.heap3d.interfaces.factories.IVirtualMachineProvider;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.ListeningConnector;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by oskar on 29/10/14.
 */
public class VirtualMachineProvider implements IVirtualMachineProvider {

    // --- Attaching connector

    @Override
    public VirtualMachine connect(int port) {
        try {
            return connect(getAttachingConnector(), Integer.toString(port));
        }
        catch(IOException | IllegalConnectorArgumentsException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private AttachingConnector getAttachingConnector() {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        return vmManager.attachingConnectors().get(0);
    }

    private VirtualMachine connect(AttachingConnector connector, String port)
            throws IllegalConnectorArgumentsException, IOException {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        args.get("port").setValue(port);

        return connector.attach(args);
    }

    // --- Listening connector

    @Override
    public ConnectedProcess establish(int port, Callable<Process> p) {
        try {
            return establish(getListeningConnector(), Integer.toString(port), p);
        }
        catch(IOException | IllegalConnectorArgumentsException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private ListeningConnector getListeningConnector() {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        return vmManager.listeningConnectors().get(0);
    }

    private ConnectedProcess establish(ListeningConnector connector, String port, Callable<Process> p)
            throws IllegalConnectorArgumentsException, IOException {
        Map<String, Connector.Argument> args = connector.defaultArguments();
        args.get("port").setValue(port);

        connector.startListening(args);
        Process pr = null;
        try {
            pr = p.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ConnectedProcess(connector.accept(args), pr);
    }
}