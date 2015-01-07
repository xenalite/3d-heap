package com.heap3d.implementations.application;

import com.heap3d.utilities.Check;
import com.heap3d.interfaces.application.IBreakpointManager;
import com.heap3d.interfaces.jdi.IEventRequestManager;
import com.heap3d.interfaces.jdi.IVirtualMachine;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.WatchpointRequest;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointManager implements IBreakpointManager {

    private Map<String, Entry<List<String>, List<String>>> _breakpointsToBeRequested;
    private Map<Location, List<BreakpointRequest>> _cachedBreakpointRequests;
    private Map<Field, List<WatchpointRequest>> _cachedWatchpointRequests;
    private IVirtualMachine _virtualMachine;
    private IEventRequestManager _eventRequestManager;

    public BreakpointManager(IVirtualMachine virtualMachine) {
        _virtualMachine = Check.notNull(virtualMachine, "virtualMachine");
        _eventRequestManager = Check.notNull(_virtualMachine.getEventRequestManager(), "eventRequestManager");
        _breakpointsToBeRequested = new HashMap<>();
        _cachedBreakpointRequests = new HashMap<>();
        _cachedWatchpointRequests = new HashMap<>();
    }

    public void notifyClassLoaded(ReferenceType referenceType) {
        String name = referenceType.name();
        if (_breakpointsToBeRequested.containsKey(name)) {
            List<String> watchpoints = _breakpointsToBeRequested.get(name).getValue();
            for (String watchpoint : watchpoints)
                createAndCacheWatchpoint(referenceType, watchpoint);

            List<String> breakpoints = _breakpointsToBeRequested.get(name).getKey();
            for (String breakpoint : breakpoints)
                createAndCacheBreakpoint(referenceType, breakpoint);
        }
    }

    public void addWatchpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _virtualMachine.classesByName(className);
        if (!classes.isEmpty()) {
            createAndCacheWatchpoint(classes.get(0), argument);
        } else {
            List<String> entries;
            if (_breakpointsToBeRequested.containsKey(className)) {
                entries = _breakpointsToBeRequested.get(className).getValue();
            } else {
                _eventRequestManager.createClassPrepareRequest(className);
                entries = new Vector<>();
                _breakpointsToBeRequested.put(className, new SimpleImmutableEntry<>(new Vector<>(), entries));
            }
            entries.add(argument);
        }
    }

    public void addBreakpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _virtualMachine.classesByName(className);
        if (!classes.isEmpty()) {
            createAndCacheBreakpoint(classes.get(0), argument);
        } else {
            List<String> entries;
            if (_breakpointsToBeRequested.containsKey(className)) {
                entries = _breakpointsToBeRequested.get(className).getKey();
            } else {
                _eventRequestManager.createClassPrepareRequest(className);
                entries = new Vector<>();
                _breakpointsToBeRequested.put(className, new SimpleImmutableEntry<>(entries, new Vector<>()));
            }
            entries.add(argument);
        }
    }

    public void removeBreakpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _virtualMachine.classesByName(className);
        if (!classes.isEmpty()) {
            Location l = classes.get(0).methodsByName(argument).get(0).location();
            List<BreakpointRequest> brs = _cachedBreakpointRequests.get(l);
            if (brs != null && !brs.isEmpty()) {
                _eventRequestManager.deleteEventRequest(brs.get(0));
                _cachedBreakpointRequests.remove(l);
            }
        }
    }

    public void removeWatchpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _virtualMachine.classesByName(className);
        if (!classes.isEmpty()) {
            Field f = classes.get(0).fieldByName(argument);
            List<WatchpointRequest> mwrs = _cachedWatchpointRequests.get(f);
            if (mwrs != null && !mwrs.isEmpty()) {
                _eventRequestManager.deleteEventRequest(mwrs.get(0));
                _cachedWatchpointRequests.remove(f);
            }
        }
    }

    private void createAndCacheBreakpoint(ReferenceType referenceType, String method) {
        BreakpointRequest br = _eventRequestManager.createBreakpointRequest(referenceType, method);
        addEntryToCachedRequests(_cachedBreakpointRequests, br.location(), br);
    }

    private void createAndCacheWatchpoint(ReferenceType referenceType, String field) {
        ModificationWatchpointRequest mwr
                = _eventRequestManager.createModificationWatchpointRequest(referenceType, field);
        addEntryToCachedRequests(_cachedWatchpointRequests, mwr.field(), mwr);
    }

    private <K,V> void addEntryToCachedRequests(Map<K, List<V>> map, K key, V value) {
        if(map.containsKey(key)) {
            List<V> list = map.get(key);
            list.add(value);
        }
        else {
            List<V> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        }
    }
}
