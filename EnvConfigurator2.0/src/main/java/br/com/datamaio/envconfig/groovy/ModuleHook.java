package br.com.datamaio.envconfig.groovy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.datamaio.fwk.io.FileUtils;

public class ModuleHook extends Hook {
	public String modulePath;

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
}