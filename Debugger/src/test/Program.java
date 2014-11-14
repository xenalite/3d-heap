package test;

/**
 * Created by oskar on 29/10/14.
 */
public class Program {

    private static int PI = 3;
    private static String NAME = "PROGRAM";
    private static String[] expressions = {"x = x + 1", "d/dx (x^2 + 2x + 2)"};

    private int counter = 74;
    private String message = "hello";

    public static void main(String[] args) {
        int i = 0;
        int j = 1 + i;
        Program p = new Program();

        j = p.method(NAME, j);
        System.out.println(j);
    }

    private int method(String name, int j) {
        j += 10;

        message = append(message, name);
        NAME = message;
        counter = j - PI;

        return j;
    }

    private static String append(String p, String q) {
        return p + " " + q;
    }
}