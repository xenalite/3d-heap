package com.imperial.heap3d.application;

import com.imperial.heap3d.utilities.Check;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.*;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointManager {

    private Map<String, Entry<List<String>, List<String>>> _cachedBreakpoints;
    private Map<Location, BreakpointRequest> _cachedBreakpointRequests;
    private Map<Field, WatchpointRequest> _cachedWatchPointRequests;
    private VirtualMachine _instance;

    public BreakpointManager(VirtualMachine instance) {
        _instance = Check.NotNull(instance, "instance");
        _cachedBreakpoints = new HashMap<>();
        _cachedBreakpointRequests = new HashMap<>();
        _cachedWatchPointRequests = new HashMap<>();
    }

    public void notifyClassLoaded(ReferenceType referenceType) {
        String name = referenceType.name();
        if (_cachedBreakpoints.containsKey(name)) {
            List<String> watchpoints = _cachedBreakpoints.get(name).getValue();
            for (String watchpoint : watchpoints)
                createWatchpointRequest(referenceType, watchpoint);

            List<String> breakpoints = _cachedBreakpoints.get(name).getKey();
            for (String breakpoint : breakpoints)
                createBreakpointRequest(referenceType, breakpoint);
        }
    }

    private void createClassPrepareRequest(String filter) {
        EventRequestManager erm = _instance.eventRequestManager();
        ClassPrepareRequest cpr = erm.createClassPrepareRequest();
        cpr.addClassFilter(filter);
        cpr.enable();
    }

    public void addWatchpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _instance.classesByName(className);
        if (!classes.isEmpty()) {
            createWatchpointRequest(classes.get(0), argument);
        } else {
            List<String> entries;
            if (_cachedBreakpoints.containsKey(className)) {
                entries = _cachedBreakpoints.get(className).getValue();
            } else {
                createClassPrepareRequest(className);
                entries = new Vector<>();
                _cachedBreakpoints.put(className, new SimpleImmutableEntry<>(new Vector<>(), entries));
            }
            entries.add(argument);
        }
    }

    public void addBreakpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _instance.classesByName(className);
        if (!classes.isEmpty()) {
            createBreakpointRequest(classes.get(0), argument);
        } else {
            List<String> entries;
            if (_cachedBreakpoints.containsKey(className)) {
                entries = _cachedBreakpoints.get(className).getKey();
            } else {
                createClassPrepareRequest(className);
                entries = new Vector<>();
                _cachedBreakpoints.put(className, new SimpleImmutableEntry<>(entries, new Vector<>()));
            }
            entries.add(argument);
        }
    }

    public void removeBreakpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _instance.classesByName(className);
        if (!classes.isEmpty()) {
            Location l = classes.get(0).methodsByName(argument).get(0).location();
            EventRequestManager erm = _instance.eventRequestManager();
            BreakpointRequest br = _cachedBreakpointRequests.get(l);
            if (br != null) {
                erm.deleteEventRequest(br);
                _cachedBreakpointRequests.remove(l);
            }
        }
    }

    public void removeWatchpoint(String className, String argument) {
        if(className == null || argument == null)
            return;

        List<ReferenceType> classes = _instance.classesByName(className);
        if (!classes.isEmpty()) {
            Field f = classes.get(0).fieldByName(argument);
            EventRequestManager erm = _instance.eventRequestManager();
            WatchpointRequest br = _cachedWatchPointRequests.get(f);
            if (br != null) {
                erm.deleteEventRequest(br);
                _cachedWatchPointRequests.remove(f);
            }
        }
    }

    private void createBreakpointRequest(ReferenceType classReference, String breakpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Location l = classReference.methodsByName(breakpoint).get(0).location();
        BreakpointRequest br = erm.createBreakpointRequest(l);
        br.enable();
        _cachedBreakpointRequests.put(l, br);
    }

    private void createWatchpointRequest(ReferenceType classReference, String watchpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Field f = classReference.fieldByName(watchpoint);
        ModificationWatchpointRequest mwr = erm.createModificationWatchpointRequest(f);
        mwr.enable();
        _cachedWatchPointRequests.put(f, mwr);
    }
}
