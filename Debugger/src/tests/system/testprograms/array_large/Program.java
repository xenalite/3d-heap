package tests.system.testprograms.array_large;


public class Program {

    public static void main(String[] args) {

        Integer[] a = new Integer[100];
        //fill the array in one step
        build(a);

        System.out.println(a);
    }

    static void build(Integer[] a)
    {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer(i);
        }
    }

}
