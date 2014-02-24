package problems;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**

You have two numbers represented by a linked list, where each node contains a single digit. The digits are stored in reverse order, such that the 1’s digit is at the head of the list. Write a function that adds the two numbers and returns the sum as a linked list.

EXAMPLE
Input: (3 -> 1 -> 5) + (5 -> 9 -> 2)       => 513 + 295 = 808
Output: 8 -> 0 -> 8

 */
public class SumInLinkedList {
	public static Node sum(Node l1, Node l2) {
		Node head = null;
		Node previous = null;
		int resto = 0;
		while (l1 != null || l2 != null) {
			int a = l1 != null ? l1.value : 0;
			int b = l2 != null ? l2.value : 0;
			int result = a + b + resto;
			if (result >= 10) {
				resto = 1;
				result = result - 10;
			} else {
				resto = 0;
			}

			Node n = new Node(result);
			if (previous == null) {
				head = n;
				previous = head;
			} else {
				previous.next = n;
				previous = n;
			}
			l1 = l1!=null ? l1.next : null;
			l2 = l2!=null ? l2.next : null;
		}
		if(resto>0){
			Node n = new Node(resto);
			previous.next = n;
		}
		return head;
	}
	
	public static class Node {
		public int value;
		public Node next;;
		public Node(int value) {
			this.value = value;
		}
	}
	
	@Test
	public void test1(){		
		Node n11 = new Node(3);		
		Node n12 = new Node(1);
		n11.next = n12;
		Node n13 = new Node(5);
		n12.next = n13;
		
		Node n21 = new Node(5);		
		Node n22 = new Node(9);
		n21.next = n22;
		Node n23 = new Node(2);
		n22.next = n23;
		
		Node e1 = new Node(8);		
		Node e2 = new Node(0);
		e1.next = e2;
		Node e3 = new Node(8);
		e2.next = e3;
		
		Node result = sum(n11, n21);
		while(result!=null) {
			assertThat(e1.value, is(result.value));
			e1 = e1.next;
			result = result.next;
		}
	}
	
	@Test
	public void test2(){		
		Node n11 = new Node(3);		
		Node n12 = new Node(1);
		n11.next = n12;
		Node n13 = new Node(5);
		n12.next = n13;
		
		Node n21 = new Node(5);		
		Node n22 = new Node(9);
		n21.next = n22;
		Node n23 = new Node(5);
		n22.next = n23;
		
		Node e1 = new Node(8);		
		Node e2 = new Node(0);
		e1.next = e2;
		Node e3 = new Node(1);
		e2.next = e3;
		Node e4 = new Node(1);
		e3.next = e4;

		
		Node result = sum(n11, n21);
		while(result!=null) {
			assertThat(e1.value, is(result.value));
			e1 = e1.next;
			result = result.next;
		}
	}


	@Test
	public void test3(){		
		Node n11 = new Node(3);		
		Node n12 = new Node(1);
		n11.next = n12;
		
		Node n21 = new Node(5);		
		Node n22 = new Node(9);
		n21.next = n22;
		Node n23 = new Node(5);
		n22.next = n23;
		
		Node e1 = new Node(8);		
		Node e2 = new Node(0);
		e1.next = e2;
		Node e3 = new Node(6);
		e2.next = e3;
		
		Node result = sum(n11, n21);
		while(result!=null) {
			assertThat(e1.value, is(result.value));
			e1 = e1.next;
			result = result.next;
		}
	}

}
