package tests.system.testprograms.layout_test;

import tests.system.testprograms.utilities.ListNode;
import tests.system.testprograms.utilities.TreeNode;

@SuppressWarnings("ALL")
public class Program {

    private ListNode list;
    private TreeNode tree;
    private int[] primitiveArray;
    private Integer[] objectArray;
    public static void main(String[] args) {
        TreeNode binaryTree = buildBinaryTree();
        ListNode root = buildLinkedList();
        Integer[] smallArray = buildArray(10);
        Integer[] largeArray = buildArray(100);
        Program p = new Program();
    }

    protected Program()
    {
        list = buildLinkedList();
        tree = buildBinaryTree();
        primitiveArray = new int[10];
        objectArray = buildArray(10);
    }

    private static ListNode buildLinkedList() {
        return new ListNode(new ListNode(new ListNode(new ListNode(
                new ListNode(new ListNode(new ListNode(new ListNode(
                        new ListNode(new ListNode(new ListNode(new ListNode(
                                new ListNode(new ListNode(new ListNode(new ListNode())))))))))))))));
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
