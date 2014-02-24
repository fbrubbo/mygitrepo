package problems;

import java.util.LinkedList;

public class SetOfStacks {
	int high;
	LinkedList<Stack> list = new LinkedList<>();
	int size=0;

	public SetOfStacks(int high) {
		this.high = high;
	}
	
	public int queues(){
		return list.size();
	}

	public int size(){
		return size;
	}
	
	public void push(int value) {
		if (list.size() == 0 || list.getLast().size >= high) {
			Stack nst = new Stack();
			list.addLast(nst);
		}
		Stack st = list.getLast();
		st.push(value);
		size++;
	}

	public int pop() {
		if (list.size() == 0) {
			return Integer.MAX_VALUE;
		}

		Stack st = list.getLast();
		if (st.size==0) {
			list.removeLast();
			st = list.getLast();
		}

		if (st == null) {
			return Integer.MAX_VALUE;
		}

		size--;
		return st.pop();
	}
}

class Stack {
	int size = 0;
	Node top;

	public void push(int value) {
		Node n = new Node(value);
		n.next = top;
		top = n;
		size++;
	}

	public int pop() {
		if (top != null) {
			Node c = top;
			int value = top.value;
			top = top.next;
			c.next = null;
			size--;
			return value;
		}
		return Integer.MAX_VALUE;
		
	}
}

class Node {
	int value;
	Node next;
	public Node(int value) {
		this.value = value;
}
}