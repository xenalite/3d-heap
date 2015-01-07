package com.heap3d.implementations.jdi;

import com.heap3d.utilities.Check;
import com.heap3d.interfaces.jdi.IEventSet;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;

import java.util.Iterator;

/**
 * Created by om612 on 01/12/14.
 */
public class DEventSet implements IEventSet {

    private final EventSet _eventSet;

    public DEventSet(EventSet eventSet) {
        _eventSet = Check.notNull(eventSet, "eventSet");
    }

    @Override
    public void resume() {
        _eventSet.resume();
    }

    @Override
    public Iterator<Event> iterator() {
        return _eventSet.iterator();
    }
}
