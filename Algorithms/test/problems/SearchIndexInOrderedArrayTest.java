package problems;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static problems.SearchIndexInOrderedArray.searchIndex;

import org.junit.Test;

public class SearchIndexInOrderedArrayTest {
	
	int[] arr = new int[]{3, 6, 8, 9, 10};
	
	@Test
	public void testAll(){
		assertThat(searchIndex(null, 3), is(-1));
		assertThat(searchIndex(new int[]{}, 3), is(-1));
		
		assertThat(searchIndex(arr, 2), is(-1));
		
		assertThat(searchIndex(arr, 3), is(0));
		assertThat(searchIndex(arr, 6), is(1));
		assertThat(searchIndex(arr, 8), is(2));
		assertThat(searchIndex(arr, 9), is(3));
		assertThat(searchIndex(arr, 10), is(4));
	}
	
}
