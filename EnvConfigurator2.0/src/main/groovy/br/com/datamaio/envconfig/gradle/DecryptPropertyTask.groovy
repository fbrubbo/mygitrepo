package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.util.Encryptor

class DecryptPropertyTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		println "\nHelper tool to decrypt an existing property"
		
		Console console = System.console()
		if (console) {
			def encrypted = console.readLine('\nEncrypted Property Value: ')
			def pass = new String(console.readPassword('Password: '))
			
			try {
				def value = Encryptor.get(pass).decrypt(encrypted);
				println("\nDecrypted value is : $value");
			} catch (Exception e) {
				println("\nIt was not possible to decrypt the property value. Check the password!");
			}			
		} else {
			println "ERROR: Cannot get console."
		}		
    }
}
