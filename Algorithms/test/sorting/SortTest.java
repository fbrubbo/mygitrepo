package sorting;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public abstract class SortTest {
	public abstract Sort get();
	
	@Test
	public void sortEmptyArray() {
		int[] in = new int[] {};
		int[] ordered = get().sort(in);
		assertThat(ordered, is(equalTo(in)));
	}

	@Test
	public void sortOneElement() {
		int[] in = new int[] { 1 };
		int[] ordered = get().sort(in);
		assertThat(ordered, is(equalTo(in)));
	}

	@Test
	public void sortTwoOrderedElements() {
		int[] in = new int[] { 1, 2 };
		int[] ordered = get().sort(in);
		assertThat(ordered, is(equalTo(in)));
	}

	@Test
	public void sortTwoElements() {
		int[] in = new int[] { 2, 1 };
		int[] ordered = get().sort(in);
		int[] expected = new int[] { 1, 2 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void sortEvenOrderedElements() {
		int[] in = new int[] { 1, 5, 6, 8 };
		int[] ordered = get().sort(in);
		assertThat(ordered, is(equalTo(in)));
	}

	@Test
	public void sortOddOrderedElements() {
		int[] in = new int[] { 1, 5, 6, 8, 9 };
		int[] ordered = get().sort(in);
		assertThat(ordered, is(equalTo(in)));
	}

	@Test
	public void sortEvenElements() {
		int[] in = new int[] { 6, 5, 8, 1 };
		int[] ordered = get().sort(in);
		int[] expected = new int[] { 1, 5, 6, 8 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void sortOddElements() {
		int[] in = new int[] { 6, 9, 5, 8, 1 };
		int[] ordered = get().sort(in);
		int[] expected = new int[] { 1, 5, 6, 8, 9 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void sortEvenInverseOrderedElements() {
		int[] in = new int[] { 8, 6, 5, 1 };
		int[] ordered = get().sort(in);
		int[] expected = new int[] { 1, 5, 6, 8 };
		assertThat(ordered, is(equalTo(expected)));
	}

	@Test
	public void sortOddInverseOrderedElements() {
		int[] in = new int[] { 9, 8, 6, 5, 1 };
		int[] ordered = get().sort(in);
		int[] expected = new int[] { 1, 5, 6, 8, 9 };
		assertThat(ordered, is(equalTo(expected)));
	}
}