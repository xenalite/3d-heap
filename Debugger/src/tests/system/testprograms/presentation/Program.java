package tests.system.testprograms.presentation;

import tests.system.testprograms.utilities.ListNode;
import tests.system.testprograms.utilities.TreeNode;

@SuppressWarnings("ALL")
public class Program {

    private ListNode list;
    private TreeNode tree;
    private int[] primitiveArray;
    private Integer[] objectArray;
    public static void main(String[] args) {
        ListNode root = buildLinkedList();
        TreeNode binaryTree = buildBinaryTree();
        Integer[] smallArray = buildArray(10);
    }

    protected Program()
    {
        list = buildLinkedList();
        tree = buildBinaryTree();
        primitiveArray = new int[10];
        objectArray = buildArray(10);
    }

    private static ListNode buildLinkedList() {
        ListNode n1 = new ListNode();
        ListNode n2 = new ListNode();
        n1.next = n2;
        ListNode n3 = new ListNode(new ListNode(new ListNode(
                        new ListNode(new ListNode(new ListNode(new ListNode(
                                new ListNode(new ListNode(new ListNode(new ListNode()))))))))));
        n2.next = n3;
        return n1;
    }

    private static TreeNode buildBinaryTree() {
        return new TreeNode(new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                new TreeNode(new TreeNode(), new TreeNode())),
                new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                        new TreeNode(new TreeNode(), new TreeNode())));
    }

    static Integer[] buildArray(int size)
    {
        Integer[] a = new Integer[size];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer(i);
        }
        return a;
    }


}
