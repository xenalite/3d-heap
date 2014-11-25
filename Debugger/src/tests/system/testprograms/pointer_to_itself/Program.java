package tests.system.testprograms.pointer_to_itself;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by zhouyou_robert on 25/11/14.
 */
public class Program {
    public static void main(String[] args) {
        ListNode l = new ListNode();
        l.next = l;
        System.out.println(l);
    }


}
