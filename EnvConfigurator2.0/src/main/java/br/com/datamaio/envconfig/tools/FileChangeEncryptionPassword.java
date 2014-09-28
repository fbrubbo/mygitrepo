package br.com.datamaio.envconfig.tools;

import java.io.File;
import java.util.Iterator;

import br.com.datamaio.envconfig.util.Encryptor;

import org.apache.commons.configuration.PropertiesConfiguration;

public class FileChangeEncryptionPassword {
	private String currentPass;
	private String newPass;
	private String path;
	
	public FileChangeEncryptionPassword(String currentPass, String newPass, String path) {
		this.currentPass = currentPass;
		this.newPass = newPass;
		this.path = path;
	}
	
	public void validate() {
		Encryptor decryptor = Encryptor.get(this.currentPass);
		Encryptor encryptor = Encryptor.get(this.newPass);
		
		try {
			File f = new File(path);
			if(f.exists()) {
				PropertiesConfiguration props = new PropertiesConfiguration(f);
				Iterator<String> keys = props.getKeys();
				int count = 0;
				while (keys.hasNext()) {
					String key = keys.next();
					String encryptedValue = (String) props.getString(key);
					if(encryptedValue!=null && encryptedValue.startsWith(Encryptor.PREFIX)) {
						count++;
						try {
							String decrypt = decryptor.decrypt(encryptedValue);
							String newEncryptedValue = encryptor.encryptProp(decrypt);
							System.out.println(count + ". Convertendo " + key + "=> de " + encryptedValue + " para " + newEncryptedValue);
							props.setProperty(key, newEncryptedValue);
						} catch (Exception e) {								
							System.out.println("\t***********************************************************************************************************");
							System.out.println("\t********************** PROBLEMA DE CRIPTOGRAFIA NA PROPRIEDADE: " + key + " *****************");
							System.out.println("\t***********************************************************************************************************");
						}		
					}
				}
				System.out.println("\n\nTOTAL DE CHAVES CRIPTOGRAFADAS ENCONTRADAS: " + count);
				props.save();
				System.out.println("\n\nArquivo " + path + " salvo com sucesso!");
			} else {
				System.out.println("Arquivo '" + path + "' n�o existe!");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args)  throws Exception {
		if (args.length != 3) {
			printHowTo();
		} else {
			String currentPass = args[0];
			String newPass = args[1];
			String filePpath = args[2];
			if (currentPass==null || currentPass.isEmpty() 
					|| newPass == null || newPass.isEmpty() 
					|| filePpath == null || filePpath.isEmpty()) {
				printHowTo();
			} else {
				new FileChangeEncryptionPassword(currentPass, newPass, filePpath).validate();
			}
		}
	}

	private static void printHowTo() {
		System.out.println("Chamar este tool com 2 parametros: (1) senha atual (2) nova senha (3) caminho do arquivo a ser validado");
	}
}
