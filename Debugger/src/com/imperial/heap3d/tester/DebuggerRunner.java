package com.imperial.heap3d.tester;

/**
 * Created by zhouyou_robert on 23/11/14.
 */
public class DebuggerRunner {
	
    private static final String PATH = System.getProperty("user.dir") + "/out/production/Debugger/";
    private static final String NAME = "test_programs.small_stack.Program";

    public static void main(String[] args) {
    	DebuggerController d = new DebuggerControllerBuilder().setClassPath(PATH).setClassName(NAME).getDebuggerController();
    	d.addBreakpoint(NAME, "main");
    	d.start();

    	// 	for(int i = 0; i < 3; i++) {
    	try {
    		Thread.sleep(1000);
    	} catch (InterruptedException ex) {
    		Thread.currentThread().interrupt();
    	}
    	//		d.step();
    	d.screenShot("ScreenShot/pic");
    	//  	}
    	try {
    		Thread.sleep(1000);
    	} catch (InterruptedException ex) {
    		Thread.currentThread().interrupt();
    	}
    	d.stop();
    }
}
