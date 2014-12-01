package com.imperial.heap3d.implementations.jdi;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.imperial.heap3d.implementations.application.ProcessState;
import com.imperial.heap3d.implementations.utilities.Check;
import com.imperial.heap3d.interfaces.jdi.IEventRequestManager;
import com.imperial.heap3d.interfaces.jdi.IEventSet;
import com.imperial.heap3d.interfaces.jdi.IVirtualMachine;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;

import static com.imperial.heap3d.implementations.application.ProcessState.*;

/**
 * Created by om612 on 01/12/14.
 */
public class DVirtualMachine implements IVirtualMachine {

    private final VirtualMachine _jdiVirtualMachine;
    private final EventBus _processStateEventBus;
    private final IEventRequestManager _eventRequestManager;
    private ProcessState _processState;

    public DVirtualMachine(VirtualMachine jdiVirtualMachine, EventBus processStateEventBus) {
        _jdiVirtualMachine = Check.NotNull(jdiVirtualMachine, "jdiVirtualMachine");
        _eventRequestManager = new DEventRequestManager(_jdiVirtualMachine.eventRequestManager());
        _processStateEventBus = Check.NotNull(processStateEventBus, "processStateEventBus");
        _processStateEventBus.register(this);
        _processState = RUNNING;
    }

    @Override
    public void resume() {
        if(_processState == PAUSED || _processState == PAUSED_AT_LOCATION) {
            _jdiVirtualMachine.resume();
            _processStateEventBus.post(RUNNING);
        }
    }

    @Override
    public void pause() {
        if(_processState == RUNNING) {
            _jdiVirtualMachine.suspend();
            _processStateEventBus.post(PAUSED);
        }
    }

    @Override
    public IEventSet waitFor(long timeout) {
        EventQueue eventQueue = _jdiVirtualMachine.eventQueue();
        IEventSet set = new NullEventSet();
        try {
            EventSet eventSet = eventQueue.remove(timeout);
            set = new DEventSet(this, eventSet);
        }
        catch (InterruptedException ignored) {}
        return set;
    }

    @Subscribe
    @Override
    public void notifyStateChange(ProcessState newState) {
        _processState = newState;
    }

    @Override
    public IEventRequestManager getEventRequestManager() {
        return _eventRequestManager;
    }

    @Override
    public EventQueue getEventQueue() {
        return _jdiVirtualMachine.eventQueue();
    }
}
