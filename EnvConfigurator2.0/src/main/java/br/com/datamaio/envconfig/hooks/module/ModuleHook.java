package br.com.datamaio.envconfig.hooks.module;

import java.util.logging.Logger;

import br.com.datamaio.envconfig.hooks.Hook;

public abstract class ModuleHook extends Hook {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected String moduleDir;
	
	@Override
	public void log(String msg) {
		LOGGER.info(msg);
	}

	protected void setModuleDir(String moduleDir) {
		this.moduleDir = moduleDir;
	}	
}