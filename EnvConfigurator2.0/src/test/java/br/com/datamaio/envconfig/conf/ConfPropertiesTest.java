package br.com.datamaio.envconfig.conf;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import org.junit.Test;

public class ConfPropertiesTest {
	private StringReader reader = new StringReader("key1=value1\n" 
			+ "key2=value2\n" 
			+ "key3=value3\n" 
			+ "key4=value4/${key3}\n"
			+ "key5=value5/${key4}");

	@Test
	public void testSipleProp() throws Exception {
		ConfProperties props = new ConfProperties();
		props.load(reader);

		assertThat(props.resolve("${key1}"), is(equalTo("value1")));
	}

	@Test
	public void testTwoOrMoreProps() throws Exception {
		ConfProperties props = new ConfProperties();
		props.load(reader);

		assertThat(props.resolve("${key1}--basdf--${key1}"), is(equalTo("value1--basdf--value1")));
		assertThat(props.resolve("${key1}--basdf--${key2}"), is(equalTo("value1--basdf--value2")));
		assertThat(props.resolve("${key1}--basdf--${key2}--basdf--${key3}"), is(equalTo("value1--basdf--value2--basdf--value3")));
	}

	@Test
	public void testRecursiveProps() throws Exception {
		ConfProperties props = new ConfProperties();
		props.load(reader);

		assertThat(props.resolve("${key1}--basdf--${key3}"), is(equalTo("value1--basdf--value3")));
		assertThat(props.resolve("${key1}--basdf--${key4}"), is(equalTo("value1--basdf--value4/value3")));
		assertThat(props.resolve("${key1}--basdf--${key5}"), is(equalTo("value1--basdf--value5/value4/value3")));
	}

	@Test
	public void testRecursiveSystemPriorityProps() throws Exception {
		try {
			System.setProperty("key4", "systemkey4");
			ConfProperties props = new ConfProperties();
			props.load(reader);

			assertThat(props.resolve("${key1}--basdf--${key3}"), is(equalTo("value1--basdf--value3")));
			assertThat(props.resolve("${key1}--basdf--${key4}"), is(equalTo("value1--basdf--systemkey4")));
			assertThat(props.resolve("${key1}--basdf--${key5}"), is(equalTo("value1--basdf--value5/systemkey4")));
		} finally {
			System.setProperty("key4", "value4/${key3}");
		}
	}

	@Test
	public void testSystemGetsPrefferenceProps() throws Exception {

		try {
			System.setProperty("key1", "systemkey1");
			ConfProperties props = new ConfProperties();
			props.load(reader);

			assertThat(props.resolve("${key1}--basdf--${key2}--basdf--${key3}"), is(equalTo("systemkey1--basdf--value2--basdf--value3")));
		} finally {
			System.setProperty("key1", "value1");
		}
	}
}
