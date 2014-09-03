package br.com.datamaio.envconfig.groovy;

import groovy.lang.GroovyObject;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;


public class ModuleHookEmbeddedGroovy extends EmbeddedGroovy {
	private final String modulePath;

	public ModuleHookEmbeddedGroovy(final String suffix, final Path module, final Properties props) {
		this(suffix, module.toString(), props);
	}
	
	public ModuleHookEmbeddedGroovy(final String suffix, final String modulePath, final Properties props) {
		super(modulePath + File.separator + "Module" + suffix, props);
		this.modulePath = modulePath;
		setModulePath();
	}
	
	private void setModulePath() {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("modulePath", modulePath);
		}
	}

	@Override
	public boolean pre() {
		System.out.println("##################################################################################################################################");
		System.out.println("########### INICIO MODULE ############### " + this.modulePath + " ############## INICIO MODULE ############");
		System.out.println("##################################################################################################################################");
		return super.pre();
	}

	@Override
	public void post() {
		super.post();
		System.out.println("##################################################################################################################################");
		System.out.println("############# FIM MODULE ############### " + this.modulePath + " ############### FIM MODULE #############");
		System.out.println("##################################################################################################################################");
	}
}
