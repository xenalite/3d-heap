package com.imperial.heap3d.tester;

/**
 * Created by zhouyou_robert on 23/11/14.
 */
public class DebuggerRunner {
    static String path = "/Users/zhouyou_robert/Dropbox/university/3rd_Year/Group_Project/3d-heap/Debugger/out/production/Debugger/";
    static String name = "test_programs.small_stack.Program";

    public static void main(String[] args){
        DebuggerController d = new DebuggerControllerBuilder().setClassPath(path).setClassName(name).getDebuggerController();
        d.addBreakpoint(name, "main");
        d.start();
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            d.step();
        }
    }
}
