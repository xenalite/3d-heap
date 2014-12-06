package tests.system.testprograms.reference_becomes_null;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by oskar on 06/12/14.
 */
public class Program {

    public static void main(String[] args) {
        ListNode a = new ListNode();
        ListNode b = a;
        a = null;
    }
}
