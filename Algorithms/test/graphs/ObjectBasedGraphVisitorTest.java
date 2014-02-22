package graphs;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import graphs.ObjectBasedGraphVisitor.Node;

import org.junit.Test;

public class ObjectBasedGraphVisitorTest {
	@Test
	public void breadthFirstSearchGraphWithOnlyOneElement(){
		Node n = new Node(1);		
		
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n);
		List<Integer> ret = v.breadthFirst();
		assertThat(n.visited, is(true));
		assertThat(ret.get(0), is(1));		
	}
	
	@Test
	public void breadthFirstSearchGraphWithTwoElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.breadthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
	}
	
	@Test
	public void breadthFirstSearchGraphWithTreeElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		n2.adjacents.add(n1);
		Node n3 = new Node(3);
		n1.adjacents.add(n3);
	
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.breadthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(n3.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
		assertThat(ret.get(2), is(3));

	}
	
	@Test
	public void breadthFirstSearchGraphWithFourElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		n2.adjacents.add(n1);
		Node n3 = new Node(3);
		n1.adjacents.add(n3);
		Node n4 = new Node(4);
		n3.adjacents.add(n4);
		n4.adjacents.add(n1);
	
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.breadthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(n3.visited, is(true) );
		assertThat(n4.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
		assertThat(ret.get(2), is(3));
		assertThat(ret.get(3), is(4));
	}
	
	@Test
	public void depthFirtsGraphWithOnlyOneElement(){
		Node n = new Node(1);		
		
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n);
		List<Integer> ret = v.depthFirst();
		assertThat(n.visited, is(true));
		assertThat(ret.get(0), is(1));		
	}
	
	@Test
	public void depthFirstGraphWithTwoElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.depthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
	}
	
	@Test
	public void depthFirtsGraphWithTreeElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		n2.adjacents.add(n1);
		Node n3 = new Node(3);
		n1.adjacents.add(n3);
	
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.depthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(n3.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
		assertThat(ret.get(2), is(3));

	}
	
	@Test
	public void depthFirtsGraphWithFourElements(){
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		n1.adjacents.add(n2);
		n2.adjacents.add(n1);
		Node n3 = new Node(3);
		n1.adjacents.add(n3);
		Node n4 = new Node(4);
		n3.adjacents.add(n4);
		n4.adjacents.add(n1);
	
		ObjectBasedGraphVisitor v = new ObjectBasedGraphVisitor(n1);
		List<Integer> ret = v.depthFirst();
		assertThat(n1.visited, is(true) );
		assertThat(n2.visited, is(true) );
		assertThat(n3.visited, is(true) );
		assertThat(n4.visited, is(true) );
		assertThat(ret.get(0), is(1));
		assertThat(ret.get(1), is(2));
		assertThat(ret.get(2), is(3));
		assertThat(ret.get(3), is(4));
	}
}
