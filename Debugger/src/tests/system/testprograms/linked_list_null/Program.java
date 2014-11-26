package tests.system.testprograms.linked_list_null;

/**
 * Created by om612 on 25/11/14.
 */
public class Program {

    public static void main(String[] args) {
        int a = 0;
        int b = 0;
        int c = 0;

        int i = method(10);
        System.out.println(i);
    }

    public static int method(int i) {
        if(i == 0)
            return 1;
        return i * method(i - 1);
    }
}
