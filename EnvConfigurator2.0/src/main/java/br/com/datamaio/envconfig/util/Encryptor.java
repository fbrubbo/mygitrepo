package br.com.datamaio.envconfig.util;

import java.io.Console;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;

public class Encryptor {
	public static final String ENCRYPTOR_PASSWORD_PROPERTY = "br.com.datamaio.envconfig.util.Encryptor.password";
	public static final String PREFIX = "ENCRYPTED:";	
	private static Encryptor INSTANCE;	
	private static int passRetry = 3;	
	
	private final BasicTextEncryptor encriptor;
	private boolean retry;
       
	public static synchronized final Encryptor get(){
		if(INSTANCE==null) {
			String pass = System.getProperty(ENCRYPTOR_PASSWORD_PROPERTY);
			if(pass==null || pass.length()==0) {
				pass = readPasswordFromConsole();
			}
			INSTANCE = new Encryptor(pass);
		}
		return INSTANCE;
	}
	
	public static final Encryptor get(String pass){
		return new Encryptor(pass, false);
	}

	
	public static synchronized final void retryPass(){
		String pass = readPasswordFromConsole();
		INSTANCE = new Encryptor(pass);
	}
		
	//OBS: isto tranca no eclipse.. pois ele nao possui um console. 
	private static String readPasswordFromConsole() {
		if(passRetry--==0) {
			throw new RuntimeException("Exiting.. Retryies (3x)!");
		}
		
		final Console cons = System.console();
		if (cons != null ) {
			final char[] passwd = cons.readPassword("\nProvide the password to decrypt properties:") ;
			if (passwd != null) {
				String pass = new String(passwd);
				System.setProperty(ENCRYPTOR_PASSWORD_PROPERTY, pass);
				return pass;
			}
		}
			
		throw new RuntimeException("It was not possible to read password!");
	}

	private Encryptor(String pass) {
		this(pass, true);
	}
	    
    private Encryptor(String pass, boolean retry) {
    	if(pass==null || pass.length()==0) {
    		throw new RuntimeException("It is required to provide the system property " 
    				+ ENCRYPTOR_PASSWORD_PROPERTY + "=<PASSWORD> (or execute it in a command line)");
    	}
    	
    	this.encriptor = new BasicTextEncryptor();
    	this.encriptor.setPassword(pass);
    	this.retry = retry;
    }
    
    public String decrypt(String text) {    	
    	try {
			String _text = text;
			if(_text.startsWith(PREFIX)) {
				_text = _text.substring(PREFIX.length());
			}
			return encriptor.decrypt(_text);
		} catch (EncryptionOperationNotPossibleException e) {
			System.out.println("\tIncorrect password!");
			if(retry) {
				retryPass();
				return INSTANCE.decrypt(text);
			} else {				
				throw new DecryptionException("It was not possible to decrypt the property!");
			}
		}
    }
    
    public String encrypt(String text){   	
    	return encriptor.encrypt(text);
    }
    
    public String encryptProp(String text) {
		return PREFIX + encrypt(text);
    }
    
    public static final class DecryptionException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public DecryptionException(String message) {
			super(message);
		}    	
    }
}