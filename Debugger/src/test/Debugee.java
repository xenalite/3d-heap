package test;

/**
 * Created by oskar on 29/10/14.
 */
public class Debugee {
    private static int static_foo;
    private int foo;
    private String[] contents = {"hello", "world"};

    public static void main(String[] args) {
        new Debugee().run();
    }

    public void run() {
        static_foo = 1;
        for(int i = 0; i < 3; ++i) {
            foo = i;
            System.out.println(foo);
        }
    }
}
