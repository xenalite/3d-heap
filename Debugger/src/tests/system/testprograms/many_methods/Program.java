package tests.system.testprograms.many_methods;

/**
 * Created by zhouyou_robert on 27/11/14.
 */
public class Program {
    static int i = 0;

    public static void succ() {
        System.out.println("In succ");
        i++;
        i--;
        i++;
    }

    public static void foo() {
        succ();
        succ();
        succ();
        sub();
        sub();
        sub();
    }

    public static void bar() {
        succ();
        sub();
        succ();
        sub();
        succ();
        sub();
    }

    public static void sub() {
        System.out.println("In sub");
        i--;
        i++;
        i--;
        i++;
        i--;
        i++;
        i--;
    }

    public static void main(String[] args) {
        foo();
        bar();
    }
}
