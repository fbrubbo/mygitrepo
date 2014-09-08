package br.com.datamaio.envconfig.groovy;

import java.io.File;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.Configuration;


public class ModuleHookEmbeddedGroovy extends EmbeddedGroovy {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private final Configuration conf;
	
	public ModuleHookEmbeddedGroovy(Configuration conf) {
		super(buildModuleHookName(conf), conf);
		this.conf = conf;
	}

	@Override
	public boolean pre() {
		LOGGER.info("##################################################################################################################################");
		LOGGER.info("########### INICIO MODULE ############### " + conf.getInstalationModule() + " ############## INICIO MODULE ############");
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
		LOGGER.info("############# FIM MODULE ############### " + conf.getInstalationModule() + " ############### FIM MODULE #############");
		LOGGER.info("##################################################################################################################################");
	}
	
	private static String buildModuleHookName(Configuration conf) {
		return conf.getInstalationModule() + File.separator + "Module" + Configuration.HOOK_SUFFIX;
	}
}
