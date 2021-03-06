package trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BTree<E extends Comparable<E>> {

	private Node<E> root;
	private int size = 0;

	public int size() {
		return size;
	}

	// outra op��o.. mas esta, apesar de ser mais elegante, navega 2x no grafo
	public boolean isBalanced2(){
		return maxDepth(root) - minDepth(root) <= 1;
	}
	int maxDepth(Node<E> n) {
		if(n==null)
			return 0;
		return 1 + Math.max(maxDepth(n.left), maxDepth(n.right)); 
	}
	int minDepth(Node<E> n) {
		if(n==null)
			return 0;
		return 1 + Math.min(maxDepth(n.left), maxDepth(n.right)); 
	}

	
	int min;
	int max;
	public boolean isBalanced(){
		if(root==null)
			return true;
		
		min = Integer.MAX_VALUE;
		max = 0;
		checkIfItIsBalanced(root, 1);
		return max - min <=1;
	}

	void checkIfItIsBalanced(Node<E> n, int h){
		if(n.left==null || n.right==null) {
			if (h<min) min = h;
			if (h>max) max = h;
		}
		
		h++;
		if(n.left!=null)
			checkIfItIsBalanced(n.left, h);
		if(n.right!=null)
			checkIfItIsBalanced(n.right, h);
	}

	
	public void add(E value) {
		Node<E> n = new Node<>(value);
		if (root == null) {
			root = n;
		} else {
			Node<E> parent = findParent(n);
			parent.insert(n);
		}
		size++;
	}

	public void remove(E value) {
		Node<E> n = get(value).node;
		if (n == null)
			return;

		if (n.hasOnlyLeftChild()) {
			insertChildToNodeParent(n, n.left);
		} else if (n.hasOnlyRightChild()) {
			insertChildToNodeParent(n, n.right);
		} else {

			Node<E> suc = sucessor(n);
			if (n.hasBothChildren() && suc.hasNoChildren()) {
				suc.left = n.left;
				insertChildToNodeParent(n, suc);
			} else if(n.hasNoChildren()) {
				insertChildToNodeParent(n, null);
			}
	
			// FIXME: not finished. Need to improve the whole method.
		}

		size--;
	}

	public boolean search(E value) {
		return steps(value) > 0 ? true : false;
	}

	public List<E> breadthFirstSteps() {
		List<E> result = new ArrayList<>();
		Queue<Node<E>> queue = new LinkedList<>();
		queue.add(root);
		while(!queue.isEmpty()) {
			Node<E> node = queue.poll();
			result.add(node.value);
			if(node.left!=null)
				queue.add(node.left);
			if(node.right!=null)
				queue.add(node.right);
		}
		return result;
	}
	
	public E minimum() {
		if (root == null)
			return null;

		return minimum(root).value;
	}

	public E maximum() {
		if(root==null)
				return null;
		
		return maximum(root).value;
	}

	private Node<E> maximum(Node<E> node) {
		Node<E> current = node;
		while (current.right != null) {
			current = current.right;
		}
		return current;
	}

	public List<E> inorder() {
		List<E> list = new ArrayList<>();
		inorder(list, root);
		return list;
	}

	public List<E> preorder() {
		List<E> list = new ArrayList<>();
		preorder(list, root);
		return list;
	}

	public List<E> postorder() {
		List<E> list = new ArrayList<>();
		postorder(list, root);
		return list;
	}

	int steps(E value) {
		return get(value).steps;
	}
	
	E sucessor(E value) {
		Node<E> node = get(value).node;
		if (node == null)
			return null;

		Node<E> suc = sucessor(node);
		return suc != null ? suc.value : null;
	}

	Node<E> sucessor(Node<E> node) {
		if (node.right != null)
			return minimum(node.right);

		Node<E> parent = node.parent;
		if (parent == null) {
			return null;
		} else if (parent.value.compareTo(node.value) <= 0) {
			if(parent.parent==null || parent.parent.value.compareTo(node.value) <= 0) {
				return null;
			} else {
				return parent.parent;
			}
		}
		return parent;
	}
	
	Node<E> minimum(Node<E> node) {
		Node<E> current = node;
		while (current.left != null) {
			current = current.left;
		}
		return current;
	}

	private Pair<E> get(E value) {
		int steps = 1;
		Node<E> current = root;
		while (current != null && current.value.compareTo(value)!=0 ) {
			if (value.compareTo(current.value) <= 0) {
				current = current.left;
			} else {
				current = current.right;
			}
			steps++;
		}

		return current != null ? new Pair<E>(steps, current) : new Pair<E>(-1, null);
	}

	private void inorder(List<E> list, Node<E> current) {
		if (current == null)
			return;

		inorder(list, current.left);
		list.add(current.value);
		inorder(list, current.right);
	}

	private void preorder(List<E> list, Node<E> current) {
		if (current == null)
			return;

		list.add(current.value);
		preorder(list, current.left);
		preorder(list, current.right);
	}

	private void postorder(List<E> list, Node<E> current) {
		if (current == null)
			return;

		postorder(list, current.left);
		postorder(list, current.right);
		list.add(current.value);
	}

	private Node<E> findParent(Node<E> n) {
		Node<E> parent;
		Node<E> current = root;
		do {
			parent = current;
			if (n.value.compareTo(current.value) <= 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		} while (current != null);

		return parent;
	}
	
	private void insertChildToNodeParent(Node<E> n, Node<E> child) {
		if (child != null)
			child.parent = n.parent;

		if (n.parent != null) {
			if (n.parent.left == n)
				n.parent.left = child;
			else
				n.parent.right = child;
		} 
	}

	static class Node<E2 extends Comparable<E2>> {
		Node<E2> parent;
		Node<E2> left;
		Node<E2> right;
		E2 value;

		public Node(E2 value) {
			this.value = value;
		}

		public void insert(Node<E2> n) {
			n.parent = this;
			if (n.value.compareTo(this.value) < 0) {
				this.left = n;
			} else {
				this.right = n;
			}
		}

		public boolean hasChildren() {
			return numberOfChildren() > 0;
		}

		public boolean hasNoChildren() {
			return numberOfChildren() == 0;
		}

		public boolean hasBothChildren() {
			return numberOfChildren() == 2;
		}

		public boolean hasOnlyLeftChild() {
			return left != null && right == null;
		}

		public boolean hasOnlyRightChild() {
			return left == null && right != null;
		}

		public int numberOfChildren() {
			int count = 0;
			if (left != null)
				count++;
			if (right != null)
				count++;
			return count;
		}

		@Override
		public String toString() {
			return "Node [value=" + value + "]";
		}
	}

	class Pair<E2 extends Comparable<E2>> {
		int steps;
		Node<E2> node;

		public Pair(int steps, Node<E2> node) {
			this.steps = steps;
			this.node = node;
		}
	}

}
