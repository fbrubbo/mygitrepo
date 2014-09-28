package br.com.datamaio.envconfig.gradle

import org.apache.commons.configuration.PropertiesConfiguration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.util.Encryptor

class ChangePasswordTask  extends DefaultTask {
	
	@TaskAction
    def action() {
		def config = Input.config(project)
		println "\nHelper tool to change the password of file : $config"
		
		if( Input.validateConfig(config) ) {
			if( config.text.contains(Encryptor.PREFIX) ) {
				Console console = System.console()
				if (console) {				
					def currentPass = new String(console.readPassword('\nCurrent Password: '))
					def newPass = new String(console.readPassword('New Password: '))
					def cNewPass = new String(console.readPassword('Confirm New Password: '))
					if (newPass==cNewPass) {
						changePassword(currentPass, newPass, config);
					} else {
						println "==============================="
						println "=== Password does not match ==="
						println "==============================="
					}
				} else {
					println "ERROR: Cannot get console."
				}
			} else {
				println "Provided config file does not have any encrypted property"
				println "============================="
				println "=== Nothing has been done ==="
				println "============================="
			}
		} else {
			println "============================="
			println "=== Nothing has been done ==="
			println "============================="
		}
    }
	
	void changePassword(currentPass, newPass, config) {
		Encryptor decryptor = Encryptor.get(currentPass);
		Encryptor encryptor = Encryptor.get(newPass);
		
		try {
			int count = 0;
			PropertiesConfiguration props = new PropertiesConfiguration(config);
			Iterator<String> keys = props.getKeys();
			while (keys.hasNext()) {
				String key = keys.next();
				String encryptedValue = (String) props.getString(key);
				if(encryptedValue!=null && encryptedValue.startsWith(Encryptor.PREFIX)) {
					count++;
					try {
						String decrypt = decryptor.decrypt(encryptedValue);
						String newEncryptedValue = encryptor.encryptProp(decrypt);
						props.setProperty(key, newEncryptedValue);
						println "$count. Converting property $key => from $encryptedValue to $newEncryptedValue"
					} catch (Exception e) {
						println "\t***********************************************************************************************************"
						println "\t********************** COULD NOT DECRYPT PROPERTY: " + key + " *****************"
						println "\t***********************************************************************************************************"
					}
				}
			}
			println "\n\nNUMBER OF ENCRYPTED PROPERTIES FOUND: $count";
			props.save();
			println "\n\nConfig file $config successfully save!";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
