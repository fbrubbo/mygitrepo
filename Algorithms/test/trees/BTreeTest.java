package trees;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BTreeTest {
	private BTree<Integer> tree;

	@Before
	public void setup() {
		tree = new BTree<Integer>();
		tree.add(2);
		tree.add(4);
		tree.add(3);
		tree.add(1);
		tree.add(5);
		tree.add(6);
	}

	@Test
	public void checkcheckIfItIsBalanced() {
		assertThat(tree.isBalanced(), is(false));
		
		BTree<Integer> tree2 = new BTree<Integer>();
		assertThat(tree2.isBalanced(), is(true));

		tree2 = new BTree<Integer>();
		tree2.add(2);
		assertThat(tree2.isBalanced(), is(true));
		
		tree2 = new BTree<Integer>();
		tree2.add(2);
		tree2.add(4);
		assertThat(tree2.isBalanced(), is(true));
		
		tree2 = new BTree<Integer>();
		tree2.add(2);
		tree2.add(4);
		tree2.add(3);
		assertThat(tree2.isBalanced(), is(false));
		
		tree2 = new BTree<Integer>();
		tree2.add(2);
		tree2.add(4);
		tree2.add(3);
		tree2.add(1);
		assertThat(tree2.isBalanced(), is(true));
	}
	
	@Test
	public void checkAddElements() {
		assertThat(tree.size(), is(6));

		assertThat(tree.steps(2), is(1));
		assertThat(tree.steps(4), is(2));
		assertThat(tree.steps(3), is(3));
		assertThat(tree.steps(1), is(2));
		assertThat(tree.steps(5), is(3));
		assertThat(tree.steps(6), is(4));
		assertThat(tree.steps(0), is(-1));
	}

	@Test
	public void checkMinimum() {
		Integer m = tree.minimum();
		assertThat(m, is(1));
	}

	@Test
	public void checkMaximum() {
		Integer m = tree.maximum();
		assertThat(m, is(6));
	}

	@Test
	public void sucessorElement1() {
		Integer suc = tree.sucessor(1);
		assertThat(suc, is(2));
	}

	@Test
	public void sucessorElement2() {
		Integer suc = tree.sucessor(2);
		assertThat(suc, is(3));
	}

	@Test
	public void sucessorElement3() {
		Integer suc = tree.sucessor(3);
		assertThat(suc, is(4));
	}

	@Test
	public void sucessorElement4() {
		Integer suc = tree.sucessor(4);
		assertThat(suc, is(5));
	}

	@Test
	public void sucessorElement5() {
		Integer suc = tree.sucessor(5);
		assertThat(suc, is(6));
	}

	@Test
	public void sucessorElement6() {
		Integer suc = tree.sucessor(6);
		assertThat(suc, is(nullValue()));
	}

	@Test
	public void sucessorElement0() {
		Integer suc = tree.sucessor(0);
		assertThat(suc, is(nullValue()));
	}
	
	@Test
	public void checkInorder() {
		List<Integer> list = tree.inorder();
		List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void checkPreorder() {
		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 4, 3, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void checkPostorder() {
		List<Integer> list = tree.postorder();
		List<Integer> expected = Arrays.asList(1, 3, 6, 5, 4, 2);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeANotFoundElement() {
		tree.remove(0);

		assertThat(tree.size(), is(6));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 4, 3, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeFromOneElementTree() {
		tree = new BTree<Integer>();
		tree.add(1);
		tree.remove(1);

		assertThat(tree.size(), is(0));
	}

	@Test
	public void removeElement1() {
		tree.remove(1);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 4, 3, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeElement3() {
		tree.remove(3);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 4, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeElement6() {
		tree.remove(6);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 4, 3, 5);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeElement5() {
		tree.remove(5);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 4, 3, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeElement4() {
		tree.remove(4);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(2, 1, 5, 3, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void removeElement2() {
		tree.remove(2);

		assertThat(tree.size(), is(5));

		List<Integer> list = tree.preorder();
		List<Integer> expected = Arrays.asList(3, 1, 5, 4, 6);
		assertThat(list, is(equalTo(expected)));
	}

	@Test
	public void breadthFirstSteps(){
		List<Integer> list = tree.breadthFirstSteps();
		List<Integer> expected = Arrays.asList(2, 1, 4, 3, 5, 6);
		assertThat(list, is(equalTo(expected)));
	}
	

}
