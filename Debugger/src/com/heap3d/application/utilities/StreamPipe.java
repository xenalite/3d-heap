package com.heap3d.application.utilities;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhouyou_robert on 09/11/14.
 */
public class StreamPipe implements Runnable{
    InputStream _is;
    StringProperty _st;

    public StreamPipe(StringProperty st) {
        this._st = st;
    }
    public void setInputStream(InputStream is){
        this._is = is;
    }
    @Override
    public void run() {
        if (_is != null) {
            try {
                InputStreamReader isr = new InputStreamReader(_is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while (true) {
                    line = br.readLine();
                    //String origStr = _st.toString();
                    //Platform.runLater(() -> _st.setValue(_st.get() + System.lineSeparator() + line));
                    System.out.println("jkasfdhkjfdashdjlafkhldfakhfdaskljhsfdakljdashfkljfda" + _st.get());
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}