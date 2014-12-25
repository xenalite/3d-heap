package tests.system.testprograms.linked_list_self_reference;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by oskar on 21/11/14.
 */
public class Program {

    public static void main(String[] args) {
        ListNode root = new ListNode();
        root.next = root;

        System.out.println(root);
    }
}
