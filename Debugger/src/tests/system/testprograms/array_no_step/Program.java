package tests.system.testprograms.array_no_step;


public class Program {

    public static void main(String[] args) {

        Integer[] a = build();

        System.out.println(a);
    }

    static Integer[] build()
    {
        Integer[] a = new Integer[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer(i);
        }
        return a;
    }

}
