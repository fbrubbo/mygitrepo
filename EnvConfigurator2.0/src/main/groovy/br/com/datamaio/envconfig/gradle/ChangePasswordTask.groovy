package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.tools.FileChangeEncryptionPassword

class ChangePasswordTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
		Console console = System.console()
		if (console) {
			def install = project.envconfig.install
			def conf = project.file("conf/" + install.conf)
			
			if(!conf.exists()) {
				println "**************************************************"
				println "*** Arquivo de configuracao '$conf' nao existe ***"
				println "**************************************************"
				return
			}
			
			def oldPass = new String(console.readPassword('\nOld Password: '))
			def newPass = new String(console.readPassword('New Password: '))
			
			FileChangeEncryptionPassword v = new FileChangeEncryptionPassword(oldPass, newPass, conf.getAbsolutePath());
			v.validate();
			
		} else {
			println "ERROR: Cannot get console."
		}
			
    }
}
