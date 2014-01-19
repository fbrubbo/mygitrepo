package problems;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InverseMatrixTest {

	@Test
	public void tc1(){
		int[][] m = new int[][]{};
		InverseMatrix.inverse(m);
		assertThat(m, is(equalTo(m)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void tc2(){
		int[][] m = new int[][]{{1, 2}};
		InverseMatrix.inverse(m);
		assertThat(m, is(equalTo(m)));
	}
	
	@Test
	public void tc3(){
		int[][] m = new int[][]{{1, 2},
								{3, 4}};
		InverseMatrix.inverse(m);
		int[][] expected = new int[][]{	{4, 3},
										{2, 1}};
		assertThat(m, is(equalTo(expected)));
	}

	@Test
	public void tc4(){
		int[][] m = new int[][]{{1, 2, 3},
								{4, 5, 6},
								{7, 8, 9}};
		InverseMatrix.inverse(m);
		int[][] expected = new int[][]{ {9, 8, 7},
										{6, 5, 4},
										{3, 2, 1}};
		assertThat(m, is(equalTo(expected)));
	}

}
