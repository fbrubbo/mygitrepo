package br.com.datamaio.envconfig.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import br.com.datamaio.envconfig.util.Encryptor.DecryptionException;

public class EncryptorTest {
	
	@Test
	public void testDescrypt(){
		Encryptor enc = Encryptor.get("abc123");
		String value = enc.encrypt("encryption test");		
		assertThat(enc.decrypt(value), is(equalTo("encryption test")));		
	}
	
	@Test (expected=DecryptionException.class)
	public void testIncorrectDescrypt(){
		Encryptor enc = Encryptor.get("abc123");
		String value = enc.encrypt("encryption test");
		
		Encryptor enc2 = Encryptor.get("123abc");
		assertThat(enc2.decrypt(value), is(equalTo("encryption test")));
	}
}
