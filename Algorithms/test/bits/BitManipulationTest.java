package bits;

import static bits.BitManipulation.LINUX_ALL;
import static bits.BitManipulation.LINUX_EXECUTE;
import static bits.BitManipulation.LINUX_READ;
import static bits.BitManipulation.LINUX_WRITE;
import static bits.BitManipulation.eight;
import static bits.BitManipulation.four;
import static bits.BitManipulation.minusOne;
import static bits.BitManipulation.one;
import static bits.BitManipulation.sexteen;
import static bits.BitManipulation.two;
import static bits.BitManipulation.zero;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BitManipulationTest {

	@Test
	public void testNumbersAsBits(){
		assertThat(minusOne(), is(-1));
		assertThat(zero(), is(0));
		assertThat(one(), is(1));
		assertThat(two(), is(2));
		assertThat(four(), is(4));
		assertThat(eight(), is(8));		
		assertThat(sexteen(), is(16));
	}
	
	@Test
	public void testShiftLeft(){
		assertThat(0b001 << 1, is(0b010));
		assertThat(0b001 << 2, is(0b100));
		assertThat(0b001 << 3, is(0b1000));
		assertThat(-0b001 << 3, is(-0b1000));
	}
	
	@Test
	public void testShiftRight(){
		assertThat(0b001 >> 1, is(0b000));
		assertThat(0b011 >> 1, is(0b001));
		assertThat(-0b011 >> 1, is(-0b010));  //??? Porque não é -0b001 como com números positivos?
		assertThat(-0b100 >> 1, is(-0b010));
		assertThat(-0b101 >> 1, is(-0b011));  //??? mesma coisa, porque aqui não é 0b010
	}
	
	@Test
	public void testBitWiseOperations(){
		assertThat(0b1010 & 0b0101, is(0b0000));
		assertThat(0b1100 & 0b0110, is(0b0100));
		
		assertThat(0b1010 | 0b0101, is(0b1111));
		assertThat(0b1100 | 0b0110, is(0b1110));

		assertThat(0b1010 ^ 0b0101, is(0b1111));
		assertThat(0b1100 ^ 0b0110, is(0b1010));	
		
//		assertThat(~0b1111 , is(0b0000));		 // Porque fala?
//		assertThat(~0b0011 , is(0b1100));        // Porque fala?
		
		
		// modelo de permissão do linux
		assertThat(LINUX_READ + LINUX_WRITE, is(0b110));
		assertThat(LINUX_WRITE + LINUX_EXECUTE, is(0b011));
		assertThat(LINUX_ALL, is(0b111));
		// dá para usar o & para saber se tem permissão: 
		// motivo.. como cada pemissão tem apenas um bit 1. fazendo um & eu verifico se na permissão total tem o bit ativo
		assertThat(LINUX_ALL & LINUX_READ, is(LINUX_READ));
		assertThat(LINUX_ALL & LINUX_WRITE, is(LINUX_WRITE));
		assertThat(LINUX_ALL & (LINUX_WRITE + LINUX_EXECUTE), is(LINUX_WRITE + LINUX_EXECUTE));
		assertThat((LINUX_WRITE + LINUX_EXECUTE) & LINUX_READ, is(not(LINUX_READ)));
	}
}

