package com.imperial.heap3d.application;

import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ModificationWatchpointRequest;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

/**
 * Created by om612 on 19/11/14.
 */
public class BreakpointManager {

    private Map<String, Entry<Vector<String>, Vector<String>>> _cachedBreakpoints;
    private VirtualMachine _instance;

    public BreakpointManager(VirtualMachine instance) {
        _instance = instance;
        _cachedBreakpoints = new HashMap<>();
    }

    public void notifyClassLoaded(ReferenceType referenceType) {
        String name = referenceType.name();
        if(_cachedBreakpoints.containsKey(name)) {
            Vector<String> watchpoints = _cachedBreakpoints.get(name).getValue();
            for(String watchpoint : watchpoints)
                createWatchpointRequest(referenceType, watchpoint);

            Vector<String> breakpoints = _cachedBreakpoints.get(name).getKey();
            for(String breakpoint : breakpoints)
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
        List<ReferenceType> classes = _instance.classesByName(className);
        if(!classes.isEmpty()) {
            createWatchpointRequest(classes.get(0), argument);
        }
        else {
            Vector<String> entries;
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
        List<ReferenceType> classes = _instance.classesByName(className);
        if(!classes.isEmpty()) {
            createBreakpointRequest(classes.get(0), argument);
        }
        else {
            Vector<String> entries;
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

    private void createBreakpointRequest(ReferenceType classReference, String breakpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Location l = classReference.methodsByName(breakpoint).get(0).location();
        BreakpointRequest br = erm.createBreakpointRequest(l);
        br.enable();
    }

    private void createWatchpointRequest(ReferenceType classReference, String watchpoint) {
        EventRequestManager erm = _instance.eventRequestManager();
        Field f = classReference.fieldByName(watchpoint);
        ModificationWatchpointRequest mwe = erm.createModificationWatchpointRequest(f);
        mwe.enable();
    }
}
