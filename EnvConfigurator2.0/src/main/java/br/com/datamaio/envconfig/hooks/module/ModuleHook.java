package br.com.datamaio.envconfig.hooks.module;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.datamaio.envconfig.hooks.Hook;
import br.com.datamaio.fwk.io.FileUtils;

public abstract class ModuleHook extends Hook {
	protected String moduleDir;

	public void unzip(String depName, String toDir) {
		Path from = resolveDependency(depName);
		command.unzip(from.toString(), toDir);;
	}

	public void rpm(String depName) {
		Path path = resolveDependency(depName);
		command.installLocalPack(path.toString());
	}

	public void deployPackages(Path deploymentPath) {
		String baseDir = null;
		try {
			baseDir = new java.io.File(".").getCanonicalFile().toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Path packsToInstall = Paths.get(baseDir, "packsToDeploy", "bin");

		log("-------------------------------------------------------------------------------------------------------------------------------------");
		log("- Instalando pacotes WAR definidos em 'packsToDeploy/bin/' no diretorio '$deploymentPath' ...");
		log("-------------------------------------------------------------------------------------------------------------------------------------");
		FileUtils.copy(packsToInstall, deploymentPath, "*.war");

		log("-------------------------------------------------------------------------------------------------------------------------------------");
		log("- Instalando pacotes EAR definidos em 'packsToDeploy/bin/' no diretorio '$deploymentPath' ...");
		log("-------------------------------------------------------------------------------------------------------------------------------------");
		FileUtils.copy(packsToInstall, deploymentPath, "*.ear");
	}

	protected void setModuleDir(String moduleDir) {
		this.moduleDir = moduleDir;
	}
}