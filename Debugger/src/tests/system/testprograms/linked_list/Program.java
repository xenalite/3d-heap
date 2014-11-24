package tests.system.testprograms.linked_list;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by oskar on 21/11/14.
 */
public class Program {

    public static void main(String[] args) {
        ListNode root = new ListNode(new ListNode(new ListNode(new ListNode(
                new ListNode(new ListNode(new ListNode(new ListNode(
                        new ListNode(new ListNode(new ListNode(new ListNode(
                                new ListNode(new ListNode(new ListNode(new ListNode())))))))))))))));

        System.out.println(root);
    }
}
