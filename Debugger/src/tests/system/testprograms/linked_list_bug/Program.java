package tests.system.testprograms.linked_list_bug;

public class Program {

    public static void main(String[] args) {
    	
        LinkedList sortedList = new LinkedList();
        
        sortedList.addCorrectly(2);
        sortedList.addCorrectly(4);
        sortedList.addCorrectly(6);
        sortedList.add(5);
    }
}

class ListNode{

    public ListNode next;
    public int value;

    public ListNode() {}

    public ListNode(int value) { 
    	this.value = value;
    	}
}

class LinkedList{
	
	private ListNode head;
	
	public void add(int value){
		
		if(head == null){
			head = new ListNode(value);
			return;
		}
		
		ListNode n = head;
		ListNode last = null;
		
		while(n != null && n.value <= value){
			 last = n;
			 n = n.next;
		}
		
		ListNode toBeAdded = new ListNode(value);
		
		last.next = toBeAdded;
		toBeAdded.next = last.next;
	}
	
	public void addCorrectly(int value){
		
		if(head == null){
			head = new ListNode(value);
			return;
		}
		
		ListNode n = head;
		ListNode last = null;
		
		while(n != null && n.value <= value){
			 last = n;
			 n = n.next;
		}
		
		ListNode toBeAdded = new ListNode(value);
		
		toBeAdded.next = last.next;
		last.next = toBeAdded;
	}
}
