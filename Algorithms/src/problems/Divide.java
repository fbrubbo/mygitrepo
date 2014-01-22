package problems;

import static java.math.MathContext.DECIMAL128;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Write a function that divides two numbers without using the divide '/' operator."
 */
public class Divide {
	public static double divide(double x, double y){
		if(y == 0)
				throw new IllegalArgumentException("y can not be 0");
		
		BigDecimal _x = new BigDecimal(x);
		BigDecimal _y = new BigDecimal(y);
		return _x.multiply(_y.pow(-1, DECIMAL128))
				.doubleValue();
		
//		return x * Math.pow(y, -1);
	}
	

	@Test(expected=IllegalArgumentException.class)
	public void tc0(){
		assertThat(divide(6,  0), is(6.0));
	}
	
	@Test
	public void tc1(){
		assertThat(divide(6,  7), is(0.85714285714285714285714285714286));
		assertThat(divide(6,  6), is(1.0));
		assertThat(divide(6,  5), is(1.2)); // falha quando usa-se o Math.pow() diretamente pois o resultado dá 1.2000000000000002
		assertThat(divide(6,  4), is(1.5));
		assertThat(divide(6,  3), is(2.0));
		assertThat(divide(6,  2), is(3.0));
		assertThat(divide(6,  1), is(6.0));

	}
	
}
