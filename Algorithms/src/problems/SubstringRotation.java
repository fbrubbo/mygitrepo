package problems;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Assume you have a method isSubstring which checks if one word is a substring
 * of another. Given two strings, s1 and s2, write code to check if s2 is a
 * rotation of s1 using only one call to isSubstring (i.e., “waterbottle” is a
 * rotation of “erbottlewat”).
 */
public class SubstringRotation {

	public static boolean isRotation(String s1, String s2) {
		if(s1==null || s2==null || s1.length()==0 || s1.length()!=s2.length())
			return false;

		String temp = s1 + s1;
		return temp.contains(s2); // isSubsring
	}

	@Test
	public void test(){
		assertThat(isRotation(null, ""), is(false));		
		assertThat(isRotation("", null), is(false));
		assertThat(isRotation(null, null), is(false));
		assertThat(isRotation("", ""), is(false));
		assertThat(isRotation("a", ""), is(false));
		
		assertThat(isRotation("waterbottle", "erbottlewat"), is(true));
		assertThat(isRotation("apple", "pleap"), is(true));

		assertThat(isRotation("apple", "ppale"), is(false));
	}


	
}
