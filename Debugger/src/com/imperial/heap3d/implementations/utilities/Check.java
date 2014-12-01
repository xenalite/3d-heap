package com.imperial.heap3d.implementations.utilities;

/**
 * Created by oskar on 28/11/14.
 */
public class Check {

    public static <T> T NotNull(T object, String name) {
        if(object == null)
            throw new IllegalArgumentException(String.format("%s -- null exception!", name));
        return object;
    }
}
