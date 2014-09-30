package br.com.datamaio.envconfig.hooks.module;

import java.nio.file.Path;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.hooks.Hook;

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
	
	protected void installDependency(String depName) {
		String path = resolveDependency(depName);
		command.installFromLocalPath(path);
	}
	
	protected void unzipDependency(String depName, String toDir) {
		String from = resolveDependency(depName);
		command.unzip(from, toDir);;
	}
	
    protected String resolveDependency(String name) {
    	Path file = conf.getDependency(name);
    	if(file==null)
    		throw new RuntimeException("Could not resolve dependency: " + name);
    	
    	return file.toString();
    }
	
	@Override
	public void log(String msg) {
		LOGGER.info(msg);
	}

	protected void setModuleDir(String moduleDir) {
		this.moduleDir = moduleDir;
	}	
}