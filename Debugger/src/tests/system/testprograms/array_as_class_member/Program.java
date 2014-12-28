package tests.system.testprograms.array_as_class_member;

/**
 * Created by oskar on 29/10/14.
 */
@SuppressWarnings("all")
public class Program {

	private int[] nums = {1, 2 ,3 ,4};
    private Integer[] objects = {new Integer(1),new Integer(2),new Integer(3),new Integer(4)};
	
    public static void main(String[] args) {
        Program p = new Program();

        System.out.println(p);
    }
}