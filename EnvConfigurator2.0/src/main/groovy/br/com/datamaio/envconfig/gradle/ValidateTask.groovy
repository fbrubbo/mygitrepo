package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ValidateTask extends DefaultTask {
	
	@TaskAction
    def action() {
		def module = Input.module(project)
		def config = Input.config(project)
		println "\nHelper tool to validate config and module inputs"
				
		if(Input.validate(module, config) && Input.validateConfigEncryption(config)) {
			println "======================="
			println "=== Config is valid ==="
			println "======================="
		} else {
			println "========================="
			println "=== Config is INVALID ==="
			println "========================="
		}
    }
}
