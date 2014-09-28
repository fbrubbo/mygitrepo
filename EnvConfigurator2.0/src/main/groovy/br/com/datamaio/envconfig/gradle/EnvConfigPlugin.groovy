package br.com.datamaio.envconfig.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.api.tasks.wrapper.Wrapper;

class EnvConfigPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
		// -- cria extensao de envconfig
        project.extensions.create("envconfig", EnvConfigExtension)
        project.envconfig.extensions.create("env", EnvNestedExtension)
        project.envconfig.extensions.create("install", InstallNestedExtension)

		project.defaultTasks 'envconfig'
		
        project.configurations {
            pack2Install
        }

		// -- configura tasks
		project.task('envconfig', type:EnvConfigTask){
			description = "Executa a configuração automatizada de todo o ambiente"
		}
		project.task('encrypt', type:EncryptPropertyTask){
			description = "Executa a criptografica em uma dada propriedade"
		}
		project.task('decrypt', type:DecryptPropertyTask){
			description = "Executa a decodificacao de uma propriedade criptografada"
		}
		project.task('validate', type:ValidateTask){
			description = "Executa a validacao das criptografias das propriedades do arquivo de conf"
		}
		project.task('changePassword', type:ChangePasswordTask){
			description = "Executa a troca da senha de todas as propriedades do arquivo de conf"
		}

				

		
		// -- configura o wrapper
		project.task('wrapper', type: Wrapper) {
			description = "Generate gradle wrapper"
			gradleVersion = '2.1'
		}		
		project.tasks["wrapper"].execute()
		
		// -- configura a geração do pacote
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
