package tests.system.testprograms.array_no_step;


public class Program {

    public static void main(String[] args) {

        int[] primitiveArray = new int[100];
        Integer[] ten = build(10);
        Integer[] hundred = build(100);
        Integer[] thousand = build(1000);
    }

    static Integer[] build(int size)
    {
        Integer[] a = new Integer[size];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer(i);
        }
        return a;
    }

}
