package tests.system.testprograms.change_back_refernce;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by zhouyou_robert on 26/11/14.
 */
public class Program {
    public static void main(String[] args) {
        ListNode a = new ListNode();
        ListNode l = new ListNode(a);
        l.next = new ListNode();
        l.next = a;
        System.out.println(l);
    }
}
