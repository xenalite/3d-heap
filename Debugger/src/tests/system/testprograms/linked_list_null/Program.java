package tests.system.testprograms.linked_list_null;

import tests.system.testprograms.utilities.ListNode;

/**
 * Created by om612 on 25/11/14.
 */
public class Program {

    private ListNode root;

    public static void main(String[] args) {
        Program p = new Program();
        p.createRoot(6);
    }

    private void createRoot(int times) {
        if(times <= 0)
            return;
        ListNode ln = new ListNode();
        ln.next = root;
        root = ln;
        createRoot(times - 1);
    }
}
