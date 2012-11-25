package br.com.datamaio.env.util;

import java.io.Console;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;

import br.com.datamaio.env.Constants;

public class Encryptor {
	public static final String PREFIX = "ENCRYPTED:";	
	private static Encryptor INSTANCE;	
	private static int passRetry = 3;	
	
	private final BasicTextEncryptor encriptor;
	private boolean retry;
       
	public static synchronized final Encryptor get(){
		if(INSTANCE==null) {
			String pass = System.getProperty(Constants.ENCRYPTOR_PASSWORD_PROPERTY);
			if(pass==null || pass.length()==0) {
				pass = readPasswordFromConsole();
			}
			INSTANCE = new Encryptor(pass);
		}
		return INSTANCE;
	}
	
	public static final Encryptor get(String pass){
		//TODO: se o cara pegar por este m�todo.. n�o deveria fazer o retry e, consequentemente, n�o deveria ler do console
		return new Encryptor(pass, false);
	}

	
	public static synchronized final void retryPass(){
		String pass = readPasswordFromConsole();
		INSTANCE = new Encryptor(pass);
	}
		
	//OBS: isto tranca no eclipse.. pois ele nao possui um console. 
	private static String readPasswordFromConsole() {
		if(passRetry--==0) {
			throw new RuntimeException("Numero maximo de tentativas (3x) foi esgotado!");
		}
		
		final Console cons = System.console();
		if (cons != null ) {
			final char[] passwd = cons.readPassword("Forneca o password para descriptografar as propriedades:") ;
			if (passwd != null) {
				String pass = new String(passwd);
				System.setProperty(Constants.ENCRYPTOR_PASSWORD_PROPERTY, pass);
				return pass;
			}
		}
			
		throw new RuntimeException("Nao foi possivel ler do console a senha para descriptografar as propriedades!");
	}

	private Encryptor(String pass) {
		this(pass, true);
	}
	    
    private Encryptor(String pass, boolean retry) {
    	if(pass==null || pass.length()==0) {
    		throw new RuntimeException("E necessario informar a propriedade java -D" + 
    				Constants.ENCRYPTOR_PASSWORD_PROPERTY + "=<SEU_PASSWORD> ao iniciar o programa (ou estar em modo console). " +
    				"Esta senha e utilizada para descriptografar as configuracoes criptografadas.");
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
			System.out.println("Senha incorreta!");
			if(retry) {
				retryPass();
				return INSTANCE.decrypt(text);
			} else {				
				throw new RuntimeException("Nao foi possivel descriptografar as propriedades com a senha informada!");
			}
		}
    }
    
    public String encrypt(String text){   	
    	return encriptor.encrypt(text);
    }
    
    public String encryptProp(String text) {
		return PREFIX + encrypt(text);
    }        
}