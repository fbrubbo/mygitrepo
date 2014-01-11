package sorting;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MergeSortTest extends SortTest {

	@Override
	public MergeSort get() {
		return new MergeSort();
	}

	// ----
	@Test
	public void mergeOneElementEachLeftLesser() {
		int[] left = new int[] { 1 };
		int[] rigth = new int[] { 2 };
		int[] ordered = get().merge(left, rigth);
		int[] expected = new int[] { 1, 2 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void mergeOneElementEachLeftBigger() {
		int[] left = new int[] { 2 };
		int[] rigth = new int[] { 1 };
		int[] ordered = get().merge(left, rigth);
		int[] expected = new int[] { 1, 2 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void mergeManyElementEach() {
		int[] left = new int[] { 1, 5 };
		int[] rigth = new int[] { 2, 7 };
		int[] ordered = get().merge(left, rigth);
		int[] expected = new int[] { 1, 2, 5, 7 };
		assertThat(ordered, is(equalTo(expected)));
	}
	
	@Test
	public void mergeOddElementInRight() {
		int[] left = new int[] { 7 };
		int[] rigth = new int[] { 3, 5 };
		int[] ordered = get().merge(left, rigth);
		int[] expected = new int[] { 3, 5, 7 };
		assertThat(ordered, is(equalTo(expected)));
	}
}