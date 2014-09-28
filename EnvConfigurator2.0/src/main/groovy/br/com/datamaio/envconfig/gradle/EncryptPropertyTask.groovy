package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.util.Encryptor

class EncryptPropertyTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		Console console = System.console()
		if (console) {
			def name = console.readLine('\nProperty Name: ')
			def value = new String(console.readPassword('Propert Value: '))
			def pass = new String(console.readPassword('Password: '))
			
			def encrypted = Encryptor.get(pass).encryptProp(value)
			
			println "Coloque a linha abaixo no arquivo de configuracao desejado."
			println "$name=$encrypted"			
		} else {
			println "ERROR: Cannot get console."
		}		
    }
}
