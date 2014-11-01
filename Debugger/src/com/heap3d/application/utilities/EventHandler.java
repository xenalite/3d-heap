package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.DestroyEvent;
import com.heap3d.application.events.IEvent;
import com.heap3d.application.events.StartDefinition;

import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private Process _currentProcess;
    private boolean _cancel;
    private EventBus _eventBus;
    private IVirtualMachineProvider _virtualMachineProvider;
    private ConcurrentLinkedDeque<IEvent<String>> _controlEventQueue;

    public EventHandler(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _eventBus.register(this);
    }

    @Subscribe
    public void handleDestroy(DestroyEvent e) {
        _cancel = true;
    }

    @Subscribe
    public void handleStart(IEvent<StartDefinition> e) {
        _controlEventQueue.add(new IEvent<String>() {
            @Override
            public String getContent() {
                StartDefinition sd = e.getContent();
                return CommandBuilder.buildCommand(sd.getJdk(), sd.getPath(), sd.getName(), sd.getPort());
            }
        });
    }

    @Subscribe
    public void handleResume(IEvent<Boolean> e) {

    }

    public void run() throws InterruptedException {
        while(true) {
            if(_cancel) {
                dispose();
                return;
            }
            _eventBus.post(new ChangeEvent("Tick!"));

            if(!_controlEventQueue.isEmpty()) {
                IEvent<String> e = _controlEventQueue.removeFirst();
                System.out.println(e.getContent());
                try {
                    _currentProcess = Runtime.getRuntime().exec(e.getContent());
                } catch (IOException ignored) {}
            }
            Thread.sleep(1000);
        }
    }

    private void dispose() {
        _currentProcess.destroy();
        _eventBus.unregister(this);
    }
}
