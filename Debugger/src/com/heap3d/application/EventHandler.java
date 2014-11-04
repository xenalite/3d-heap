package com.heap3d.application;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.heap3d.application.events.EventType;
import com.heap3d.application.utilities.CommandBuilder;
import com.heap3d.application.utilities.IVirtualMachineProvider;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;
import com.sun.jdi.request.EventRequestManager;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by oskar on 31/10/14.
 */
public class EventHandler {

    private Process _currentProcess;
    private boolean _cancel;
    private boolean _pause;
    private EventBus _eventBus;
    private VirtualMachine _virtualMachineInstance;
    private IVirtualMachineProvider _virtualMachineProvider;
    private ConcurrentLinkedDeque<EventDTO> _controlEventQueue;

    private class EventDTO {

//        public StartDefinition definition;

        public EventType type;
//        public BreakpointDefinition bdefinition;
    }

    public EventHandler(IVirtualMachineProvider virtualMachineProvider, EventBus eventBus) {
        _virtualMachineProvider = virtualMachineProvider;
        _eventBus = eventBus;
        _cancel = false;
        _pause = true;
        _controlEventQueue = new ConcurrentLinkedDeque<>();
        _eventBus.register(this);
    }

//    @Subscribe
//    public void handleControl(ControlEvent e) {
//        EventDTO dto = new EventDTO();
//        dto.type = e.getContent();
//        _controlEventQueue.add(dto);
//    }

//    @Subscribe
//    public void handleStart(StartEvent e) {
//        EventDTO dto = new EventDTO();
//        dto.definition = e.getContent();
//        _controlEventQueue.add(dto);
//    }
//
//    @Subscribe
//    public void handleBreakpoint(NewBreakpointEvent e) {
//        EventDTO dto = new EventDTO();
//        dto.bdefinition = e.getContent();
//        _controlEventQueue.add(dto);
//    }

    public void loop() throws InterruptedException {
//        while(true) {
//            if (_cancel)
//                return;
////            _eventBus.post(new ChangeEvent(_currentProcess != null && _currentProcess.isAlive()));
//
//            if (!_controlEventQueue.isEmpty()) {
//                EventDTO e = _controlEventQueue.removeFirst();
//                if (e.type == null) {
//                    StartDefinition sd = e.definition;
//                    String command = CommandBuilder.buildCommand(sd.getJdk(), sd.getPath(), sd.getName(), sd.getPort());
////                System.out.println(e.getContent());
////                    try {
////                        _currentProcess = Runtime.getRuntime().exec(command);
////                        System.out.print(_currentProcess.getOutputStream());
////                    Stream.concat(System.out, _currentProcess.getOutputStream());
////                    _currentProcess.getOutputStream()
////                    } catch (IOException ignored) {
////                        ignored.printStackTrace();
////                    }
//                    _virtualMachineInstance = _virtualMachineProvider.connect(sd.getPort());
//                } else if(e.bdefinition == null) {
//                    switch (e.type) {
//                        case PAUSE:
//                            _virtualMachineInstance.suspend();
//                            _pause = true;
//                            break;
//                        case RESUME:
//                            _virtualMachineInstance.resume();
//                            _pause = false;
//                            EventRequestManager erm = _virtualMachineInstance.eventRequestManager();
//                            List<ReferenceType> rt = _virtualMachineInstance.classesByName("com.heap3d.application.Debugee");
//                            for(ReferenceType r : rt) {
//                                List<Method> lm = r.methodsByName("main");
//                                for(Method m : lm) {
//                                    erm.createBreakpointRequest(m.location());
//                                }
//                            }
//                            break;
//                        case STOP:
//                            _cancel = true;
//                            break;
//                    }
//                }
//                else {
////                    System.out.println("Schedule breakpoint!");
////                    BreakpointDefinition bd = e.bdefinition;
//
//
//                }
//            }
//
//            if (_virtualMachineInstance != null && !_pause) {
////                _virtualMachineInstance.resume();
//                EventQueue queue = _virtualMachineInstance.eventQueue();
//                System.out.println("Event!");
//                EventSet set = queue.remove();
//                for (Event event : set) {
//                    if (event instanceof VMDeathEvent)
//                        return;
//                    else if (event instanceof VMStartEvent)
//                        System.out.println("VM START!");
//                    else if (event instanceof BreakpointEvent)
//                        System.out.println("Breakpoint!");
//                }
//                set.resume();
//            }
//
////            Thread.sleep(500);
//        }
    }

    public void run() throws InterruptedException {
        loop();
        dispose();
    }

    private void dispose() {
        _virtualMachineInstance.exit(0);
        _currentProcess.destroy();
        _eventBus.unregister(this);
    }
}