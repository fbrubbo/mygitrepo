package br.com.datamaio.envconfig.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class EnvConfigPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.extensions.create("envconfig", EnvConfigExtension)
        project.envconfig.extensions.create("env", EnvNestedExtension)
        project.envconfig.extensions.create("install", InstallNestedExtension)

        project.configurations {
            pack2Install
        }

		project.task('envconfig', type:EnvConfigTask){
			description = "Executa a configuração automatizada de todo o ambiente"
		}
    }
}
