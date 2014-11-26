package com.imperial.heap3d.factories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by om612 on 26/11/14.
 */
public class ThreadBuilder {

    public static ExecutorService createService(String name) {
        ExecutorService service = Executors.newSingleThreadExecutor(r -> {
           Thread t = new Thread(r, name);
            t.setDaemon(true);
            return t;
        });
        return service;
    }
}
