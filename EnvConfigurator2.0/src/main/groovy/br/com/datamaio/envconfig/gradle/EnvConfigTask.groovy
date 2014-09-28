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
		def install = project.envconfig.install
		def conf = project.file("conf/" + install.conf)
		def module = project.file("module/" + install.module)
		
		System.setProperty("user.dir", project.rootDir.absolutePath);
		
        println "=================== Running envconfig! =============================="
		println "====== Environment Properties ======"
        println "IP PROD LIST  : " + env.ipsProd
        println "IP HOM  LIST  : " + env.ipsHom
        println "IP TST  LIST  : " + env.ipsTst
		println "====== Instalation Properties ======"
        println "CONFIG FILE   : " + conf 
        println "MODULE DIR    : " + module  
		println "====================================================================="
		
		def console = System.console()
		if (console) {
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

			def ok = console.readLine('\n> Você está rodando no ambiente descrito acima. Tem certeza que deseja continuar? ')
			if("sim".equalsIgnoreCase(ok) || "yes".equalsIgnoreCase(ok) || "s".equalsIgnoreCase(ok) || "y".equalsIgnoreCase(ok)) {
				def environments = new ConfEnvironments(env.ipsProd, env.ipsHom, env.ipsTst)
				def dependencies = mapDependencies2Path();
				new EnvConfigurator(conf.toPath(), module.toPath(), environments, dependencies).exec();
			} else {
				println "***************************"
				println "*** Instalacao abortada ***"
				println "***************************"
			}
		} else {
			println "ERROR: Cannot get console."
		}
		
    }

	def mapDependencies2Path(){	
		def map = [:]		
		project.configurations.pack2Install.dependencies?.each {Dependency d ->
			def key = "$d.group:$d.name:$d.version"
			def value = project.configurations.pack2Install.files?.find { File f ->
				def name = f.toString();
				return d.group.split("\\.").find { s -> name.contains(s) }!=null && name.contains(d.name) && name.contains(d.version)
			}
			if(value==null){
				throw new InvalidUserDataException("Nao conseguiu resolver $key. Em packs2Intall NAO use '+'")
			}
			map.putAt(key, value.toPath())
		}		
		return map
	}
}
