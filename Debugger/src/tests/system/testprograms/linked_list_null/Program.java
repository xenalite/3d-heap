package tests.system.testprograms.linked_list_null;

/**
 * Created by om612 on 25/11/14.
 */
public class Program {

    private Object o1;
    private Object o2;

    public static void main(String[] args) {
        Program p1 = new Program();
        Program p2 = p1;
        Program p3 = new Program();
        Program p4 = new Program();
        Object x1 = new Object();
        Object x2 = new Object();
        p1.o1 = x1;
        p1.o2 = x1;
        p3.o1 = x1;
        p4.o1 = x2;
    }
}
