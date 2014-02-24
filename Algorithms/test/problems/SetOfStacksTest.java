package problems;

import static java.lang.Integer.MAX_VALUE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SetOfStacksTest {
	
	@Test
	public void testPopFromEmptySet(){
		SetOfStacks s = new SetOfStacks(2);
		assertThat(s.pop(), is(MAX_VALUE));
	}

	@Test
	public void testPushToEmptySet(){
		SetOfStacks s = new SetOfStacks(2);
		
		assertThat(s.queues(), is(0));
		
		s.push(1);
		
		assertThat(s.size(), is(1));
		assertThat(s.queues(), is(1));
		assertThat(s.pop(), is(1));
		assertThat(s.size(), is(0));
		assertThat(s.queues(), is(1));
	}

	@Test
	public void testPushElements(){
		SetOfStacks s = new SetOfStacks(2);
		
		assertThat(s.queues(), is(0));
		
		s.push(1);
		s.push(2);
		s.push(3);
		s.push(4);
		s.push(5);
		
		assertThat(s.size(), is(5));
		assertThat(s.queues(), is(3));
		
		assertThat(s.pop(), is(5));
		assertThat(s.queues(), is(3));
		
		assertThat(s.pop(), is(4));
		assertThat(s.queues(), is(2));

		assertThat(s.pop(), is(3));
		assertThat(s.queues(), is(2));

		assertThat(s.pop(), is(2));
		assertThat(s.queues(), is(1));

		assertThat(s.size(), is(1));
	}
	
}
