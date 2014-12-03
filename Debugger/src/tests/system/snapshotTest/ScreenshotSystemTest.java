package tests.system.snapshotTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ScreenshotSystemTest {
	
	private static final String PATH = System.getProperty("user.dir") + "/out/production/Debugger/";
	private static final int SLEEP_TIME = 500;
	
	private String classpath;
	private int linesToExecute;
	
	public ScreenshotSystemTest(String classpath, int linesToExecute) {
		this.classpath = classpath;
		this.linesToExecute = linesToExecute;
	}
	
	@Parameters//(name= "{index}: running {1} lines of program: {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "tests.system.testprograms.small_stack.Program", 3},
				{"tests.system.testprograms.binary_tree.Program", 5}, 
				{ "tests.system.testprograms.binary_tree_self_reference.Program", 3},
				{ "tests.system.testprograms.linked_list.Program", 3},
				{ "tests.system.testprograms.linked_lis_cycles.Program", 3}
				});
	}
	
	@Test
	public void testProgram(){
		DebuggerController d = null;
		try{
			d = new DebuggerControllerBuilder().setClassPath(PATH).setClassName(classpath).getDebuggerController();
			d.addBreakpoint(classpath, "main");
			d.start();
			waitAndTakeScreenShot(d, 0);
			
			for(int i = 1; i <= linesToExecute; i++) {
				d.step();
				waitAndTakeScreenShot(d, i);
			}
			
			d.stop();
			d.cleanup();
			
		}catch(Exception e){
			if(d != null){
				try{
					d.stop();
				}catch(Exception e2){}
				d.cleanup();
				throw e;
			}
		}
	}

	private void waitAndTakeScreenShot(DebuggerController d, int line){
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
}
