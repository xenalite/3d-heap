package com.heap3d.utilities;

public class Check {

    public static <T> T notNull(T object, String name) {
        if(object == null)
            throw new IllegalArgumentException(String.format("%s -- null exception!", name));
        return object;
    }
}
