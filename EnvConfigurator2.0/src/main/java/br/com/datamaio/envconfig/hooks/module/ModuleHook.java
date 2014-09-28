package br.com.datamaio.envconfig.hooks.module;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.hooks.Hook;
import br.com.datamaio.fwk.io.FileUtils;

public abstract class ModuleHook extends Hook {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected String moduleDir;
	
	// --- install methods ---
	
	public void install(String pack) {
		command.install(pack);
	}

	public void uninstall(String pack) {
		command.uninstall(pack);
	}
	
	protected void installFromRepo(String depName) {
		Path path = resolveDependency(depName);
		command.installFromLocalPath(path.toString());
	}
	
	protected void unzipFromRepo(String depName, String toDir) {
		Path from = resolveDependency(depName);
		command.unzip(from.toString(), toDir);;
	}
	
	@Override
	public void log(String msg) {
		LOGGER.info(msg);
	}

	// --- deploy methods ---
	
	protected void deployPackages(Path deploymentPath) {
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
	
	// --- private methods ---
	
    private Path resolveDependency(String name) {
    	Path file = conf.getDependency(name);
    	if(file==null)
    		throw new RuntimeException("Não foi possível resolver a dependência " + name);
    	
    	return file;
    }
}