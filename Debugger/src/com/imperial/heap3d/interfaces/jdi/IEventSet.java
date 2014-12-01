package com.imperial.heap3d.interfaces.jdi;

import com.sun.jdi.event.Event;

/**
 * Created by om612 on 01/12/14.
 */
public interface IEventSet extends Iterable<Event> {

    public void resume();
}
