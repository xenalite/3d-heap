package com.imperial.heap3d.implementations.jdi;

import com.imperial.heap3d.interfaces.jdi.IEventSet;
import com.sun.jdi.event.Event;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by om612 on 01/12/14.
 */
public class NullEventSet implements IEventSet {

    @Override
    public void resume() {}

    @Override
    public Iterator<Event> iterator() {
        return new ArrayList<Event>().iterator();
    }
}
