package test_programs.linked_list_cycles;

import test_programs.utilities.ListNode;

/**
 * Created by oskar on 21/11/14.
 */
public class Program {

    public static void main(String[] args) {
        ListNode root = new ListNode();
        root.next = new ListNode(root);
    }
}
