package com.heap3d.application;

/**
 * Created by oskar on 29/10/14.
 */
public class Debugee {
    private int foo;

    public static void main(String[] args) {
        Debugee debugee = new Debugee();

        while(true){
            debugee.foo = 8973;
            System.out.println(debugee.foo);
        }
    }
}
