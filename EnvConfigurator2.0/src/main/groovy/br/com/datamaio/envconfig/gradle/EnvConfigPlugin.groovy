package br.com.datamaio.envconfig.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.api.tasks.wrapper.Wrapper;

class EnvConfigPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
    	project.defaultTasks 'envconfig'
    	project.configurations {
    		pack2Install
    	}
		
		// -- create envconfig extension
        project.extensions.create("envconfig", EnvConfigExtension)
        project.envconfig.extensions.create("env", EnvNestedExtension)
        project.envconfig.extensions.create("install", InstallNestedExtension)		

		// -- configure new tasks
		project.task('envconfig', type:EnvConfigTask){
			description = "Automatically isntall and configure the environment"
		}
		project.task('encrypt', type:EncryptPropertyTask){
			description = "Helper to encrypt a property"
		}
		project.task('decrypt', type:DecryptPropertyTask){
			description = "Helper to dencrypt a property"
		}
		project.task('validate', type:ValidateTask){
			description = "Helper to perform a basic sanity check in the configuration (includes password check)"
		}
		project.task('changepassword', type:ChangePasswordTask){
			description = "Helper to change password for encrypted properties all at once"
		}			

		
		// -- configure the wrapper to execute automatically
		project.task('wrapper', type: Wrapper) {
			description = "Generate gradle wrapper"
			gradleVersion = '2.1'
		}		
		project.tasks["wrapper"].execute()
		
		
		// -- configure packaging 
		project.apply (plugin: 'base')
		project.task('pack', type:Zip) {
			from '.'
			exclude 'build'
			exclude 'log'
		}
		project.configurations {
			archives
		}
		project.artifacts {
			archives project.pack
		}
    }
}
