package sorting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HeapSortTest {
	
	HeapSort sort = new HeapSort();
	
	@Test(expected=IllegalArgumentException.class)
	public void heapfyWithIndex0() {
		int[] arr = new int[] { 1 };
		sort.heapfy(arr, 0);
	}

	@Test
	public void heapfyWithIndexBiggerThanArrayLength() {
		int[] arr = new int[] { 1, 2 };
		sort.heapfy(arr, 2);
		int[] expected = new int[] { 1, 2 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void heapfyOneElement() {
		int[] arr = new int[] { 1 };
		sort.heapfy(arr, 1);
		int[] expected = new int[] { 1 };
		assertThat(arr, is(equalTo(expected)));
	}

	@Test
	public void heapfyTwoElement() {
		int[] arr = new int[] { 1, 2 };
		sort.heapfy(arr, 1);
		int[] expected = new int[] { 2, 1 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void heapfyAHeapWithTwoElement() {
		int[] arr = new int[] { 2, 1 };
		sort.heapfy(arr, 1);
		int[] expected = new int[] { 2, 1 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void heapfyThreeElement() {
		int[] arr = new int[] { 1, 2, 3 };
		sort.heapfy(arr, 1);
		int[] expected = new int[] { 3, 2, 1 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void heapfyManyElementInSubThree() {
		int[] arr = new int[] { 15, 6, 4, 8, 5, 3, 1, 2, 7 };
		sort.heapfy(arr, 1);
		int[] expected = new int[] { 15, 8, 4, 7, 5, 3, 1, 2, 6 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void heapfyManyElementInSubThreeInformingTheSize() {
		int[] arr = new int[] { 2, 8, 6, 7, 5, 3, 1, 15, 16 };
		sort.heapfy(arr, 1, 7);
		int[] expected = new int[] { 8, 7, 6, 2, 5, 3, 1, 15, 16 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	
	@Test
	public void buildHeap() {
		int[] arr = new int[] { 15, 6, 16, 8, 5, 3, 1, 2, 7 };
		sort.buildHeap(arr);
		int[] expected = new int[] { 16, 8, 15, 7, 5, 3, 1, 2, 6 };
		assertThat(arr, is(equalTo(expected)));
	}

	@Test
	public void sortOneElement() {
		int[] arr = new int[] { 1 };
		sort.sort(arr);
		int[] expected = new int[] { 1 };
		assertThat(arr, is(equalTo(expected)));
	}
	
	@Test
	public void sortTwoElements() {
		int[] arr = new int[] { 1, 2 };
		sort.sort(arr);
		int[] expected = new int[] { 1, 2 };
		assertThat(arr, is(equalTo(expected)));
	}

	@Test
	public void sortManyElements() {
		int[] arr = new int[] { 15, 6, 16, 8, 5, 3, 1, 2, 7 };
		sort.sort(arr);
		int[] expected = new int[] { 1, 2, 3, 5, 6, 7, 8, 15, 16 };
		assertThat(arr, is(equalTo(expected)));
	}

}
