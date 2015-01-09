package tests.system.testprograms.static_fields;

import java.util.*;

/**
 * Created by costica on 12/31/2014.
 */
public class Program {
    // private static String x1 = "abcd";
    // private static String x2 = "efgh";
    // private static String x3 = "ijkl";
    // private static int abcd = 4000;

    private static ArrayList<String> AL = new ArrayList<>();

    // private Stack<Integer> stack = new Stack<>();
    // private static Integer[] arr = new Integer[] {2, 0, 1, 5};
    // private static Random r = new Random();
    // private static StringBuilder sb = new StringBuilder();

    // private int abcde = 100;

    private Queue<Integer> queue = new PriorityQueue<>();

    public static void main(String[] args) {

        Program p1 = new Program();

        // ArrayList<String> AL = new ArrayList<>();

        AL.add("a");
        AL.add("b");
        AL.add("c");
        AL.add("d");

        Program p2 = new Program();

        // TODO: does not update the static ArrayList...
        // AL.add("e");

        // p1.stack.push(1);
        // p1.stack.push(2);
        // p1.stack.push(3);

        // p1.stack.pop();
        // p1.stack.pop();

        // p1.stack.push(4);

        // Program p1 = new Program();

        p1.queue.add(1);
        p1.queue.add(2);
        p1.queue.add(3);

        p1.queue.remove();

        p1.queue.add(4);
        p1.queue.add(5);

        // Program p2 = new Program();
    }
}
