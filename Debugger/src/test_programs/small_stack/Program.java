package test_programs.small_stack;

/**
 * Created by oskar on 29/10/14.
 */
@SuppressWarnings("ALL")
public class Program {

	private String s = "hello";
	private int[] nums = {1, 2 ,3 ,4};
	
    public static void main(String[] args) {
        int i = 1;
        int a = 6 * i + i;
        Program p = new Program();
        i = 3 * (++a) + i;
        a = 12 * a + i;
        i = (++i) + ((++i) ^ a);
        a = 5003 - 45 * i - a;

        System.out.println(a);
    }
}