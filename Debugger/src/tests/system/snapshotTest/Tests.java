package tests.system.snapshotTest;

import org.junit.Test;

public class Tests {

	private static final String PATH = System.getProperty("user.dir") + "\\bin\\";//"/out/production/Debugger/";
    private static final String NAME = "tests.system.testprograms.small_stack.Program";
	
    @Test
    public void hello(){
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
    	d.screenShot("ScreenShot/pic1");
    	//  	}
    	try {
    		Thread.sleep(7000);
    	} catch (InterruptedException ex) {
    		Thread.currentThread().interrupt();
    	}
    	d.screenShot("ScreenShot/pic2");
    	try {
    		Thread.sleep(1000);
    	} catch (InterruptedException ex) {
    		Thread.currentThread().interrupt();
    	}
    	d.stop();
    }

}
