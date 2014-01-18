package dynamicprogramming;

import static dynamicprogramming.ArrayMultiplication.multiplyAllButCurrentElement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ArrayMultiplicationTest {

	@Test
	public void testEmptyArray(){
		int[] a = new int[]{};
		int[] out = multiplyAllButCurrentElement(a);
		assertThat(out, is(equalTo(a)));
	}

	@Test
	public void testArrayWithOnlyOneElement(){
		int[] a = new int[]{4};
		int[] out = multiplyAllButCurrentElement(a);
		assertThat(out, is(equalTo(new int[]{1})));
	}

	@Test
	public void testArrayWithtwoElements(){
		int[] a = new int[]{5, 3};
		int[] out = multiplyAllButCurrentElement(a);
		assertThat(out, is(equalTo(new int[]{3, 5})));
	}
	
	@Test
	public void testArrayWithThreeElements(){
		int[] a = new int[]{4, 2, 3};
		int[] out = multiplyAllButCurrentElement(a);
		assertThat(out, is(equalTo(new int[]{6, 12, 8})));
	}

	@Test
	public void testArrayWithManyElement(){
		int[] a = new int[]{4, 2, 3, 5};
		int[] out = multiplyAllButCurrentElement(a);
		assertThat(out, is(equalTo(new int[] { 30, 60, 40, 24 })));
	}

	
}
