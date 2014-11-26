package tests.system.testprograms.change_reference;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by zhouyou_robert on 26/11/14.
 */
public class Program {
    public static void main(String[] args) {
        ListNode l = new ListNode(new ListNode());
        l.next = new ListNode();
        System.out.println(l);
    }

}
