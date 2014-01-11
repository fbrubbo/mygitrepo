package hashtable;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class MyHashMapTest {
	MyHashMap<Integer, Integer> hash;

	@Before
	public void setup() {
		hash = new MyHashMap<>(3);
	}

	@Test
	public void putElementsWithoutColision() {
		hash.put(1, 10);
		assertThat(hash.size(), is(1));

		hash.put(2, 20);
		assertThat(hash.size(), is(2));

		hash.put(3, 30);
		assertThat(hash.size(), is(3));

		assertThat(hash.get(1), is(10));
		assertThat(hash.get(2), is(20));
		assertThat(hash.get(3), is(30));

		// ensure there is no colision
		for (MyHashMap.Entry<Integer, Integer> entry : hash.table) {
			assertThat(entry, is(not(nullValue())));
		}
	}

	@Test
	public void putTwoElementsWithSameKey() {
		hash.put(1, 10);
		assertThat(hash.size(), is(1));
		
		hash.put(1, 11);
		assertThat(hash.size(), is(1));
		assertThat(hash.get(1), is(11));
	}
	
	@Test
	public void putElementsWithColision() {
		hash.put(1, 10);
		assertThat(hash.size(), is(1));

		hash.put(4, 40);
		assertThat(hash.size(), is(2));

		hash.put(7, 70);
		assertThat(hash.size(), is(3));

		assertThat(hash.get(1), is(10));
		assertThat(hash.get(4), is(40));
		assertThat(hash.get(7), is(70));

		assertThat(hash.table[0], is(nullValue()));
		assertThat(hash.table[1], is(not(nullValue())));
		assertThat(hash.table[2], is(nullValue()));
	}

	@Test
	public void removeElementWithoutColision() {
		hash.put(1, 10);
		hash.put(2, 20);
		hash.put(3, 30);

		hash.remove(3);
		assertThat(hash.size(), is(2));

		assertThat(hash.table[0], is(nullValue()));
		assertThat(hash.table[1], is(not(nullValue())));
		assertThat(hash.table[2], is(not(nullValue())));
	}

	@Test
	public void removeFirstElementWithColision() {
		hash.put(1, 10);
		hash.put(4, 40);
		hash.put(7, 70);

		hash.remove(1);
		assertThat(hash.size(), is(2));

		assertThat(hash.get(1), is(nullValue()));
		assertThat(hash.get(4), is(40));
		assertThat(hash.get(7), is(70));
	}

	@Test
	public void removeMiddleElementWithColision() {
		hash.put(1, 10);
		hash.put(4, 40);
		hash.put(7, 70);

		hash.remove(4);
		assertThat(hash.size(), is(2));

		assertThat(hash.get(1), is(10));
		assertThat(hash.get(4), is(nullValue()));
		assertThat(hash.get(7), is(70));
	}

	@Test
	public void removeLastElementWithColision() {
		hash.put(1, 10);
		hash.put(4, 40);
		hash.put(7, 70);

		hash.remove(7);
		assertThat(hash.size(), is(2));

		assertThat(hash.get(1), is(10));
		assertThat(hash.get(4), is(40));
		assertThat(hash.get(7), is(nullValue()));
	}
}