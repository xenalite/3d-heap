package tests.system.testprograms.static_types_unique;

/**
 * This program tests that there is only one static stacknode of type Program
 */
public class Program {
    private static String x1 = "abcd";
    private static String x2 = "efgh";

    public static void main(String[] args) {
        Program p1 = new Program();
        Program p2 = new Program();
    }
}
