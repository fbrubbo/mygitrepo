package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.tools.FileEncryptionValidator;
import br.com.datamaio.envconfig.util.Encryptor

class ValidateTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		Console console = System.console()
		if (console) {
			def install = project.envconfig.install
			def conf = project.file("conf/" + install.conf)
			def module = project.file("module/" + install.module)
			
			if(!conf.exists()) {
				println "**************************************************"
				println "*** Arquivo de configuracao '$conf' nao existe ***"
				println "**************************************************"
				return
			}
			
			if(!module.exists() || !module.isDirectory()) {
				println "**********************************************************"
				println "*** Module '$module' nao existe ou nao eh um diretorio ***"
				println "**********************************************************"
				return
			}
			
			def pass = new String(console.readPassword('\nPassword: '))
			
			FileEncryptionValidator v = new FileEncryptionValidator(pass, conf.getAbsolutePath());
			v.validate();
			
		} else {
			println "ERROR: Cannot get console."
		}
			
    }
}
