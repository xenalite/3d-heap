package test_programs.linked_list;

import test_programs.utilities.ListNode;

/**
 * Created by oskar on 21/11/14.
 */
@SuppressWarnings("ALL")
public class Program {

    public static void main(String[] args) {
        ListNode root = new ListNode(new ListNode(new ListNode(new ListNode(
                new ListNode(new ListNode(new ListNode(new ListNode(
                        new ListNode(new ListNode(new ListNode(new ListNode(
                                new ListNode(new ListNode(new ListNode(new ListNode())))))))))))))));

        System.out.println(root);
    }
}