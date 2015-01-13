package tests.system.testprograms.presentation;

import sun.reflect.generics.tree.Tree;
import tests.system.testprograms.utilities.ListNode;
import tests.system.testprograms.utilities.TreeNode;

import java.util.HashMap;
import java.util.Map;

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
        Map<String,String> map = populate(new HashMap<>());
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

        TreeNode t1 = new TreeNode(new TreeNode(), new TreeNode());
        TreeNode t2 = new TreeNode(new TreeNode(), new TreeNode());
        TreeNode t3 = new TreeNode(t1, t2);
        TreeNode t4 = new TreeNode(t1, t2);
        TreeNode t5 = new TreeNode(t3, t4);

        return t5;
        /*
        return new TreeNode(new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                new TreeNode(new TreeNode(), new TreeNode())),
                new TreeNode(new TreeNode(new TreeNode(), new TreeNode()),
                        new TreeNode(new TreeNode(), new TreeNode())));
        */
    }

    static Integer[] buildArray(int size)
    {
        Integer[] a = new Integer[size];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer(i);
        }
        return a;
    }

    private static Map<String, String> populate(Map<String, String> map) {
        for(int i = 0; i < 40; ++i) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
        return map;
    }

}
