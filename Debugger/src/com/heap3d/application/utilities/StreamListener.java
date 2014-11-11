package com.heap3d.application.utilities;

import com.google.common.eventbus.EventBus;
import com.heap3d.application.events.ProcessEvent;
import com.heap3d.application.events.ProcessEventType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhouyou_robert on 09/11/14.
 */
public class StreamListener implements Runnable {

    private EventBus _eventBus;
    private BufferedReader _bufferedStream;

    public StreamListener(EventBus eventBus, InputStream processOutputStream) {
        _eventBus = eventBus;
        _bufferedStream = new BufferedReader(new InputStreamReader(processOutputStream));
    }

    @Override
    public void run() {
        try {
            String line = _bufferedStream.readLine();
            while (line != null) {
                _eventBus.post(new ProcessEvent(ProcessEventType.PROCESS_MSG, line));
                line = _bufferedStream.readLine();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}