package com.heap3d.implementations.application;

import com.google.common.eventbus.EventBus;
import com.heap3d.implementations.events.ProcessEvent;
import com.heap3d.implementations.events.ProcessEventType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhouyou_robert on 09/11/14.
 */
public class StreamListener implements Runnable {

    private EventBus _eventBus;
    private BufferedReader _bufferedOut;
    private BufferedReader _bufferedError;
    private static final String OUT_TOKEN = "[OUT] ";
    private static final String ERROR_TOKEN = "[ERROR] ";

    public StreamListener(EventBus eventBus, InputStream outputStream, InputStream errorStream) {
        _eventBus = eventBus;
        _bufferedOut = new BufferedReader(new InputStreamReader(outputStream));
        _bufferedError = new BufferedReader(new InputStreamReader(errorStream));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String outString = _bufferedOut.readLine();
                String errorString = _bufferedError.readLine();

                if(outString != null)
                    _eventBus.post(new ProcessEvent(ProcessEventType.PROCESS_MSG, OUT_TOKEN + outString));
                if(errorString != null)
                    _eventBus.post(new ProcessEvent(ProcessEventType.PROCESS_MSG, ERROR_TOKEN + errorString));
            }
        }
        catch(IOException ignored) {
        }
    }
}