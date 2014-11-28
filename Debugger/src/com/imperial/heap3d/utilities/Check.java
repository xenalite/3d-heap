package com.imperial.heap3d.utilities;

/**
 * Created by oskar on 28/11/14.
 */
public class Check {

    public static <T> T NotNull(T object) {
        if(object == null)
            throw new IllegalStateException(String.format("%s is null", object));
        return object;
    }
}
