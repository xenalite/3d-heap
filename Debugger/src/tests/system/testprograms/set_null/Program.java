package tests.system.testprograms.set_null;

import tests.system.testprograms.utilities.ListNode;

public class Program {

    public static void main(String[] args)
    {
        ListNode a = new ListNode();
        ListNode b = a;
        a = null;

        System.out.println(b);
    }
}
