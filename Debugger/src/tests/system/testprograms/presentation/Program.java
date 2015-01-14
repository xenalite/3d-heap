package tests.system.testprograms.presentation;

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

        testLinkedList();
    }

    private static void testLinkedList(){
    	
        LinkedList sortedList = new LinkedList();
        sortedList.addCorrectly(2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        
        sortedList.add(5);
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
        TreeNode t3 = new TreeNode(t1, new TreeNode());
        TreeNode t4 = new TreeNode(t3, t2);

        return t4;
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

class ListNode{

    public ListNode next;
    public int value;

    public ListNode() {}

    public ListNode(int value) {
        this.value = value;
    }

    public ListNode(int value, ListNode next) {
        this.value = value;
        this.next = next;
    }
    
    public ListNode(ListNode next) { this.next = next; }
}

class LinkedList{

    private ListNode head;

    public void add(int value){

        if(head == null){
            head = new ListNode(value);
            return;
        }

        ListNode n = head;
        
        if(n.value >= value){
        	head = new ListNode(value);
        	head.next = n;
        	return;
        }

        while(n.next != null && n.next.value <= value){
            n = n.next;
        }

        ListNode toBeAdded = new ListNode(value);

        n.next = toBeAdded;
        toBeAdded.next = n.next;
    }

    
    
    public void addCorrectly(int... args){
    	head = new ListNode(args[0]);
    	ListNode n = head;
    	for(int i = 1; i < args.length; i++){
    		ListNode newNode = new ListNode(args[i]);
    		n.next = newNode;
    		n = n.next;
    	}
    }
    
    public void addCorrectly(int value){

        if(head == null){
            head = new ListNode(value);
            return;
        }

        ListNode n = head;
        
        if(n.value >= value){
        	head = new ListNode(value);
        	head.next = n;
        	return;
        }

        while(n.next != null && n.next.value <= value){
            n = n.next;
        }

        ListNode toBeAdded = new ListNode(value);


        toBeAdded.next = n.next;
        n.next = toBeAdded;
    }
}
