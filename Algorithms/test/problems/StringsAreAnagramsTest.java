package problems;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static problems.StringsAreAnagrams.anagrams;

import org.junit.Test;

public class StringsAreAnagramsTest {
	@Test
	public void test(){
		assertThat(anagrams("", ""), is(true));
		assertThat(anagrams("", null), is(false));
		assertThat(anagrams(null, null), is(false));
		assertThat(anagrams(null, ""), is(false));
		
		assertThat(anagrams("ab c", "cb a"), is(true));
		assertThat(anagrams("ab c", "cba"), is(false));
		
		assertThat(anagrams("ab c", "cb c"), is(false));
		assertThat(anagrams("aaab c", "caab a"), is(true));
	}
}
