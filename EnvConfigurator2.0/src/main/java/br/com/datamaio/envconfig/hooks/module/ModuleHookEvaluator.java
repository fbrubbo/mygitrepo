package br.com.datamaio.envconfig.hooks.module;

import static br.com.datamaio.envconfig.conf.Configuration.HOOK_SUFFIX;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.hooks.HookEvaluator;
import br.com.datamaio.fwk.io.PathUtils;


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
		LOGGER.info("#############################################################################################################");
		LOGGER.info("######## BEGIN MODULE ############# " + relativize() + " ############ BEGIN MODULE #########");
		LOGGER.info("#############################################################################################################");
		boolean pre = super.pre();
		LOGGER.info("--------------------------" );
		return pre;
	}

	private String relativize() {
		String mod = moduleDir.toString();
		return mod.substring(mod.indexOf(File.separator + "module"));
	}

	@Override
	public void post() {
		super.post();		
	}

	@Override
	public void finish() {
		super.finish();
		LOGGER.info("#############################################################################################################");
		LOGGER.info("########## END MODULE ############## " + relativize() + " ############## END MODULE ##########");
		LOGGER.info("#############################################################################################################");
	}
	
	private static Path buildModuleHookName(Configuration conf) {
		return PathUtils.get(conf.getModuleDir(), "Module" + HOOK_SUFFIX);
	}	
}
