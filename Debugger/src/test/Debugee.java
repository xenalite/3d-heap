package test;

/**
 * Created by oskar on 29/10/14.
 */
public class Debugee {
    private int foo;

    public static void main(String[] args) {
        Debugee debugee = new Debugee();

        for(int i = 0; i < 3; ++i) {
            debugee.foo = i;
            System.out.println(debugee.foo);
        }
    }
}
