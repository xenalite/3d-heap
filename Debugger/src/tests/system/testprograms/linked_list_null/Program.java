package tests.system.testprograms.linked_list_null;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by om612 on 25/11/14.
 */
public class Program {

    public static void main(String[] args) {
        ListNode root = new ListNode();
        root.next = root;

        System.out.println(root);
    }
}
