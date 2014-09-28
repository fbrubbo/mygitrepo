package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.util.Encryptor

class DecryptPropertyTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		Console console = System.console()
		if (console) {
			def encrypted = console.readLine('\nEncrypted Property Value: ')
			def pass = new String(console.readPassword('Password: '))
			
			def value = Encryptor.get(pass).decrypt(encrypted);
			
			println("Propriedade descriptografada : $value");
			
		} else {
			println "ERROR: Cannot get console."
		}		
    }
}
