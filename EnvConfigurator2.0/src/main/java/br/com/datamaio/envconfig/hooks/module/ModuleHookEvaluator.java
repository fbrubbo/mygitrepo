package br.com.datamaio.envconfig.hooks.module;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.hooks.HookEvaluator;


public class ModuleHookEvaluator extends HookEvaluator {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private final Path moduleDir;
	
	public ModuleHookEvaluator(final Configuration conf) {
		super(buildModuleHookName(conf), buildBinding(conf), conf);
		this.moduleDir = conf.getModuleDir();
	}

	@Override
	protected String getScriptBaseClass() {
		return ModuleHook.class.getName();
	}	
	
	private static Map<String, Object> buildBinding(final Configuration conf) {
		Map<String, Object> map = new HashMap<>();
		map.put("moduleDir", conf.getModuleDir().toString());
		return map;
	}

	@Override
	public boolean pre() {
		LOGGER.info("##################################################################################################################################");
		LOGGER.info("########### INICIO MODULE ############### " + moduleDir + " ############## INICIO MODULE ############");
		LOGGER.info("##################################################################################################################################");
		return super.pre();
	}

	@Override
	public void post() {
		super.post();
	}

	@Override
	public void finish() {
		super.finish();
		LOGGER.info("##################################################################################################################################");
		LOGGER.info("############# FIM MODULE ############### " + moduleDir + " ############### FIM MODULE #############");
		LOGGER.info("##################################################################################################################################");
	}
	
	private static String buildModuleHookName(Configuration conf) {
		return conf.getModuleDir() + File.separator + "Module" + Configuration.HOOK_SUFFIX;
	}	
}
