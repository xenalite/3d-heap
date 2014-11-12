package test;

/**
 * Created by oskar on 29/10/14.
 */
public class Program {
    public static void main(String[] args) {
        int i = 9;
        int r = new Program().factorial(i);
        System.out.println(r);
        throw new RuntimeException("Hello world");
    }

    private int factorial(int n) {
        if(n == 0)
            return 1;
        int r = factorial(n - 1);
        return n * r;
    }
}