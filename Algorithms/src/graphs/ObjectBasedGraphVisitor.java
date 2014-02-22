package graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ObjectBasedGraphVisitor {
	private Node root;
	
	public ObjectBasedGraphVisitor(Node root){
		this.root = root;
	}

	public List<Integer> breadthFirst(){
		List<Integer> ret = new ArrayList<>();
		
		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		
		while(!queue.isEmpty()) {
			Node n = queue.poll();
			
			System.out.println(n.value);
			ret.add(n.value);
			n.visited = true;
			for (Node ad : n.adjacents) {
				if(!ad.visited) {
					queue.add(ad);
				}
			}			
		}
		return ret;
	}

	public List<Integer> depthFirst(){
		List<Integer> ret = new ArrayList<>();		
		depthFirst(ret, root);
		return ret;
	}

	
	private void depthFirst(List<Integer> ret, Node n) {
		if(n==null)
			return; 
		
		System.out.println(n.value);
		ret.add(n.value);
		n.visited = true;
		for (Node ad : n.adjacents) {
			if(!ad.visited) {
				depthFirst(ret, ad);
			}
		}
	}


	public static class Node {
		public int value;
		public List<Node> adjacents = new ArrayList<>();
		public boolean visited;
		
		public Node(int value) {
			this.value = value;
		}
	}
}


