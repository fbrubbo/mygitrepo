package problems;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 
 Write an algorithm such that if an element in an MxN matrix is 0, its entire row and column is set to 0.
 
 */
public class PutZeroInLineAndColumn {
	public static void zero(int[][] m) {
		if (m == null || m.length == 0)
			return;

		boolean[] izero = new boolean[m.length];
		boolean[] jzero = new boolean[m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				if (!izero[i] && !jzero[j]) {
					int value = m[i][j];
					if (value == 0) {
						izero[i] = true;
						jzero[j] = true;
						setZeroToLineAndColum(m, i, j);
					}
				} else {
					m[i][j] = 0;
				}
			}
		}
	}

	private static void setZeroToLineAndColum(int[][] m, int i, int j) {
		for (int k = 0; k <= j; k++) {
			m[i][k] = 0;
		}
		for (int k = 0; k <= i; k++) {
			m[k][j] = 0;
		}
	}
	
	@Test
	public void test(){
		int[][] in = new int[][]{	{1, 2, 3},
			 						{4, 0, 5},
			 						{6, 7, 8}};
		int[][] out = new int[][]{	{1, 0, 3},
									{0, 0, 0},
									{6, 0, 8}};
		zero(in);
		validate(in, out);
	}
	
	@Test
	public void test2(){
		int[][] in = new int[][]{	{1, 2, 3},
			 						{4, 0, 5},
			 						{6, 7, 0}};
		int[][] out = new int[][]{	{1, 0, 0},
									{0, 0, 0},
									{0, 0, 0}};
		zero(in);
		validate(in, out);
	}


	private void validate(int[][] in, int[][] out) {
		for (int i = 0; i < in.length; i++) {
			for (int j = 0; j < in[i].length; j++) {
				assertThat(in[i][j], is(out[i][j]));
			}
		}		
	}
}
