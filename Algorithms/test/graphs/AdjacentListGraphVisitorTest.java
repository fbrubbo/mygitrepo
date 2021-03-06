package graphs;

import static graphs.AdjacentListGraphVisitor.BLACK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AdjacentListGraphVisitorTest {
	
	@Test
	public void breadthFirstSearchGraphWithOnlyOneElement(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0}, new int[][]{});
		v.breadthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1})) );
		assertThat(v.finished, is(equalTo(new int[]{2})) );
		assertThat(v.parent, is(equalTo(new int[]{-1})) );
	}
	
	@Test
	public void breadthFirstSearchGraphWithOnlyTwoElementsWithoutEdges(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{});
		v.breadthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, -1})) );
	}
	
	@Test
	public void breadthFirstSearchGraphWithOnlyTwoElementsRightEdge(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{{0, 1}});
		v.breadthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, 0})) );		
	}

	@Test
	public void breadthFirstSearchGraphWithOnlyTwoElementsLeftEdge(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{{1, 0}});
		v.breadthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, -1})) );
	}

	@Test
	public void breadthFirstSearchGraphWithManyElements(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0, 1, 2, 3}, new int[][]{{0,1}, {1,3}, {3,1}, {3,0}});
		v.breadthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK, BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3, 7, 5})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4, 8, 6})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, 0, -1, 1})) );
	}
	
	// ------------

	@Test
	public void depthFirstSearchGraphWithOnlyOneElement(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0}, new int[][]{});
		v.depthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1})) );
		assertThat(v.finished, is(equalTo(new int[]{2})) );
		assertThat(v.parent, is(equalTo(new int[]{-1})) );
	}
	
	@Test
	public void depthFirstSearchGraphWithOnlyTwoElementsWithoutEdges(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{});
		v.depthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, -1})) );
	}
	
	@Test
	public void depthFirstSearchGraphWithOnlyTwoElementsRightEdge(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{{0, 1}});
		v.depthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 2})) );
		assertThat(v.finished, is(equalTo(new int[]{4, 3})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, 0})) );		
	}

	@Test
	public void depthFirstSearchGraphWithOnlyTwoElementsLeftEdge(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0,1}, new int[][]{{1, 0}});
		v.depthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{2, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, -1})) );
	}

	@Test
	public void depthFirstSearchGraphWithManyElements(){
		AdjacentListGraphVisitor v = new AdjacentListGraphVisitor(new int[]{0, 1, 2, 3}, new int[][]{{0,1}, {1,3}, {3,1}, {3,0}});
		v.depthFirstSearch();
		assertThat(v.color, is(equalTo(new int[]{BLACK, BLACK, BLACK, BLACK})) );
		assertThat(v.discovered, is(equalTo(new int[]{1, 2, 7, 3})) );
		assertThat(v.finished, is(equalTo(new int[]{6, 5, 8, 4})) );
		assertThat(v.parent, is(equalTo(new int[]{-1, 0, -1, 1})) );
	}
}
