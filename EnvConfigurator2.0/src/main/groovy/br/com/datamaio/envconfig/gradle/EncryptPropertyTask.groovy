package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.util.Encryptor

class EncryptPropertyTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		println "\nHelper tool to encrypt a new property"
		
		Console console = System.console()
		if (console) {
			def name = console.readLine('\nProperty Name: ')
			def value = new String(console.readPassword('Propert Value: '))
			def newPass = new String(console.readPassword('New Password: '))
			def cNewPass = new String(console.readPassword('Confirm New Password: '))
			if (newPass==cNewPass) {			
				def encrypted = Encryptor.get(newPass).encryptProp(value)
				println "\nPut the property bellow in the configuration file"
				println "$name=$encrypted"
			} else {
				println "==============================="
				println "=== Password does not match ==="
				println "==============================="
			}
		} else {
			println "ERROR: Cannot get console."
		}		
    }
}
