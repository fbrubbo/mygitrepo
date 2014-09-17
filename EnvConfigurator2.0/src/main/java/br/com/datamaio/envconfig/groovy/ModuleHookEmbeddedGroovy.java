package br.com.datamaio.envconfig.groovy;

import groovy.lang.GroovyObject;

import java.io.File;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.Configuration;


public class ModuleHookEmbeddedGroovy extends EmbeddedGroovy {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private final Configuration conf;
	
	public ModuleHookEmbeddedGroovy(Configuration conf) {
		super(buildModuleHookName(conf), conf);
		setConfiguration(conf);
		this.conf = conf;
	}

	@Override
	public boolean pre() {
		LOGGER.info("##################################################################################################################################");
		LOGGER.info("########### INICIO MODULE ############### " + conf.getModuleDir() + " ############## INICIO MODULE ############");
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
		LOGGER.info("############# FIM MODULE ############### " + conf.getModuleDir() + " ############### FIM MODULE #############");
		LOGGER.info("##################################################################################################################################");
	}
	
	private static String buildModuleHookName(Configuration conf) {
		return conf.getModuleDir() + File.separator + "Module" + Configuration.HOOK_SUFFIX;
	}
	
	private void setConfiguration(final Configuration conf) {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("modulePath", conf.getModuleDir());
		}
	}	
}
