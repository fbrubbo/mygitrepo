package sorting;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class QuickSortTest extends SortTest {
	@Override
	public QuickSort get() {
		return new QuickSort();
	}
	
	//---
	
	@Test	
	public void partitioningTwoOrderedElements() {
		int[] in = new int[]{1, 2};
		int result = get().partitioning(in, 0, 1);
		assertThat( result, is(0) );
		assertThat( in, is(equalTo(in)) );
	}

	@Test	
	public void partitioningTwoElements() {
		int[] in = new int[]{3, 2};
		int result = get().partitioning(in, 0, 1);
		assertThat( result, is(0) );
		int[] expected = new int[]{2,3};
		assertThat( in, is(equalTo(expected)) );
	}

	@Test	
	public void partitioningManyOrderedElementsRigthSide() {
		int[] in = new int[]{1, 2, 5, 7, 9, 10};
		int result = get().partitioning(in, 3, 5);
		assertThat( result, is(3) );
		assertThat( in, is(equalTo(in)) );
	}

	@Test	
	public void partitioningManyElementsRigthSide() {
		int[] in = new int[]{1, 5, 2, 10, 7, 9};
		int result = get().partitioning(in, 3, 5);
		assertThat( result, is(4) );
		int[] expected = new int[]{1, 5, 2, 9, 7, 10};
		assertThat( in, is(equalTo(expected)) );
	}
}