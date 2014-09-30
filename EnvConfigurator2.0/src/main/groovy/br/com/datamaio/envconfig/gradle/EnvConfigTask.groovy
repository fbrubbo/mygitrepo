package br.com.datamaio.envconfig.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.TaskAction

import br.com.datamaio.envconfig.EnvConfigurator
import br.com.datamaio.envconfig.conf.ConfEnvironments

class EnvConfigTask  extends DefaultTask {
	
	@TaskAction
    def action() {		
    	def env = project.envconfig.env		
		def config = Input.config(project);
		def module = Input.module(project)
				
        println "==================== Running envconfig =============================="
		println "====== Environment Configuration ======"
        println "IP PROD LIST  : " + env.ipsProd
        println "IP HOM  LIST  : " + env.ipsHom
        println "IP TST  LIST  : " + env.ipsTst
		println "IP DES  LIST  : [ANY OTHER]"
		println "====== Instalation Configuration ======"
        println "CONFIG FILE   : " + config 
        println "MODULE DIR    : " + module  
		println "====================================================================="
		
		if( Input.validate(module, config) ) {
			println "aqui"
			def console = System.console()
			if (console) {
				def ok = console.readLine('\nReview the above config. Type "yes" to procceed and "no" to abort: ')
				if("sim".equalsIgnoreCase(ok) || "yes".equalsIgnoreCase(ok) 
					|| "s".equalsIgnoreCase(ok) || "y".equalsIgnoreCase(ok)) {
					def environments = new ConfEnvironments(env.ipsProd, env.ipsHom, env.ipsTst)
					def dependencies = mapDependencies2Path();
					new EnvConfigurator(config.toPath(), module.toPath(), environments, dependencies).exec();
				} else {
					println "============================"
					println "=== Instalation aborted! ==="
					println "============================"
				}
			} else {
				println "ERROR: Cannot get console."
			}
		} else {
			println "============================"
			println "=== Instalation aborted! ==="
			println "============================"
		}
		
    }

	def mapDependencies2Path(){	
		def map = [:]		
		project.configurations.pack2Install.dependencies?.each {Dependency d ->
			if(d.version.contains("+")) {
				throw new InvalidUserDataException("In 'packs2Intall' it is not allowed to use '+' modifier!")
			}
			
			def key = "$d.group:$d.name:$d.version"
			def value = project.configurations.pack2Install.files?.find { File f ->
				def name = f.toString();
				return d.group.split("\\.").find { s -> name.contains(s) }!=null && name.contains(d.name) && name.contains(d.version)
			}
			if(value==null){
				throw new InvalidUserDataException("Could not resolve 'packs2Install' dependency: $key.")
			}
			map.putAt(key, value.toPath())
		}		
		return map
	}
}
