package greedy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import greedy.IntercalateLists.Pair;

import org.junit.Test;

public class IntercalateListsTest {

	@Test
	public void testIntercalateHelperMethod() {
		int[] l1 = new int[] {};
		int[] l2 = new int[] {};
		int[] out = new IntercalateLists().interc(l1, l2);
		assertThat(out, is(equalTo(new int[] {})));
	}

	@Test
	public void testIntercalateHelperMethod1() {
		int[] l1 = new int[] { 1 };
		int[] l2 = new int[] {};
		int[] out = new IntercalateLists().interc(l1, l2);
		assertThat(out, is(equalTo(new int[] {1})));
	}

	@Test
	public void testIntercalateHelperMethod2() {
		int[] l1 = new int[] {};
		int[] l2 = new int[] { 2, 3 };
		int[] out = new IntercalateLists().interc(l1, l2);
		assertThat(out, is(equalTo(new int[] {2,3})));
	}

	@Test
	public void testIntercalateHelperMethod3() {
		int[] l1 = new int[] { 1, 2 };
		int[] l2 = new int[] { 2, 3, 4 };
		int[] out = new IntercalateLists().interc(l1, l2);
		assertThat(out, is(equalTo(new int[] {1, 2, 2, 3, 4})));
	}

	@Test
	public void noListsToIntercalate() {
		Pair out = new IntercalateLists()._intercalate();
		assertThat(out.arr, is(equalTo(new int[] {})));
		assertThat(out.operations, is(0));
	}

	@Test
	public void onlyOneListToIntercalate() {
		Pair out = new IntercalateLists()._intercalate(new int[] { 10, 20 });
		assertThat(out.arr, is(equalTo(new int[] { 10, 20 })));
		assertThat(out.operations, is(0));
	}

	@Test
	public void OnlyTwoListsToIntercalate() {
		int[] l1 = new int[] { 1, 2, 3 };
		int[] l2 = new int[] { 1, 2, 3, 4, 5 };
		Pair out = new IntercalateLists()._intercalate(l1, l2);

		int[] expected = new int[] { 1, 1, 2, 2, 3, 3, 4, 5 };
		assertThat(out.arr, is(equalTo(expected)));
		assertThat(out.operations, is(7));
	}

	@Test
	public void threeListsToIntercalate() {
		int[] l1 = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] l2 = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
				15 };
		int[] l3 = new int[] { 1, 2, 3, 4, 5 };
		Pair out = new IntercalateLists()._intercalate(l1, l2, l3);

		int[] expected = new int[] { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5,
				5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 12, 13, 14, 15 };
		assertThat(out.arr, is(equalTo(expected)));
		assertThat(out.operations, is(43));
	}

}
