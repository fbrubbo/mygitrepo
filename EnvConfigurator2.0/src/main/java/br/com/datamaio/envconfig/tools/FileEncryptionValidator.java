package br.com.datamaio.envconfig.tools;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.PropertiesConfiguration;

import br.com.datamaio.envconfig.util.Encryptor;

public class FileEncryptionValidator {
	private String pass;
	private String path;
	
	public FileEncryptionValidator(String pass, String path) {
		this.pass = pass;
		this.path = path;
	}
	
	public void validate() {
		Encryptor enc = Encryptor.get(this.pass);
		
		try {
			File f = new File(path);
			if(f.exists()) {
				PropertiesConfiguration props = new PropertiesConfiguration(f);
				Iterator<String> keys = props.getKeys();
				int count = 0;
				while (keys.hasNext()) {
					String key = keys.next();
					String value = (String) props.getString(key);
					if(value!=null && value.startsWith(Encryptor.PREFIX)) {
						count++;
						System.out.println(count + ". Validando propriedade: " + key );
						try {
							enc.decrypt(value);
							System.out.println("\t..OK");
						} catch (Exception e) {								
							System.out.println("\t***********************************************************************************************************");
							System.out.println("\t********************** PROBLEMA DE CRIPTOGRAFIA NA PROPRIEDADE: " + key + " *****************");
							System.out.println("\t***********************************************************************************************************");
						}		
					}
				}
				System.out.println("\n\nTOTAL DE CHAVES CRIPTOGRAFADAS ENCONTRADAS: " + count);
			} else {
				System.out.println("Arquivo '" + path + "' n�o existe!");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args)  throws Exception {
		if (args.length != 2) {
			printHowTo();
		} else {
			String pass = args[0];
			String filePpath = args[1];
			if (pass == null || pass.length() == 0 || filePpath == null || filePpath.length() == 0) {
				printHowTo();
			} else {
				new FileEncryptionValidator(pass, filePpath).validate();
			}
		}
	}

	private static void printHowTo() {
		System.out.println("Chamar este tool com 2 parametros: (1) senha (2) caminho do arquivo a ser validado");
	}
}
