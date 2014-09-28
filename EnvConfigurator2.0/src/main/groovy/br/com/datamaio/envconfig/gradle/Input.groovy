package br.com.datamaio.envconfig.gradle

import org.apache.commons.configuration.PropertiesConfiguration

import br.com.datamaio.envconfig.util.Encryptor

class Input {
	static File config(project){
		def install = project.envconfig.install
		return project.file("config/" + install.config)
	}
	
	static File module(project){
		def install = project.envconfig.install
		return project.file("module/" + install.module)
	}
	
	static boolean validate(module, config) {
		return validateModule(module) && validateConfig(config);
	}
	
	static boolean validateModule(module) {
		if(!module.exists() || !module.isDirectory()) {
			println "***************************************************************************************"
			println "*** Module '$module' does not exists or it is not a directory ***"
			println "***************************************************************************************"
			return false
		}
		return true;
	}
	
	static boolean validateConfig(config) {
		if(!config.exists() || config.isDirectory()) {
			println "***************************************************************************************"
			println "*** Configuration '$config' does not exists or it is a directory ***"
			println "***************************************************************************************"
			return false
		}
		return true;
	}
	
	static boolean validateConfigEncryption(config) {
		boolean valid = true;
		
		if( config.text.contains(Encryptor.PREFIX) ) {
			Console console = System.console()
			if (console) {
				def pass = new String(console.readPassword('\nPassword: '))
				Encryptor enc = Encryptor.get(pass);
				try {
					int count = 0;
					PropertiesConfiguration props = new PropertiesConfiguration(config);
					Iterator<String> keys = props.getKeys();
					while (keys.hasNext()) {
						String key = keys.next();
						String value = (String) props.getString(key);
						if(value!=null && value.startsWith(Encryptor.PREFIX)) {
							count++;
							println count + ". Validating encrypted property: " + key
							try {
								enc.decrypt(value);
								println "\t..OK"
							} catch (Exception e) {
								println "\t*******************************************************************************************************************"
								println "\t********** ENCRYPTION DOES NOT MATCH THE PASSWORD FOR PROPERTY: " + key + " ***********"
								println "\t*******************************************************************************************************************"
								valid = false;
							}
						}
					}
					println "\n\nNUMBER OF ENCRYPTED PROPERTIES FOUND: $count"					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			} else {
				println "ERROR: Cannot get console."
			}
		}
		
		return valid;
	}
}
