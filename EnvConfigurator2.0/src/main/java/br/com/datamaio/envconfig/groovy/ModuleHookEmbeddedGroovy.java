package br.com.datamaio.envconfig.groovy;

import java.io.File;
import java.util.Properties;


public class ModuleHookEmbeddedGroovy extends EmbeddedGroovy {
	private final String resourcePath;

	public ModuleHookEmbeddedGroovy(final String resourcePath, final Properties props) {
		super(resourcePath + File.separator + "Hook.groovy", props);
		this.resourcePath = resourcePath;
	}

	@Override
	public boolean pre() {
		System.out.println("##################################################################################################################################");
		System.out.println("########### INICIO MODULE ############### " + this.resourcePath + " ############## INICIO MODULE ############");
		System.out.println("##################################################################################################################################");
		return super.pre();
	}

	@Override
	public void post() {
		super.post();
		System.out.println("##################################################################################################################################");
		System.out.println("############# FIM MODULE ############### " + this.resourcePath + " ############### FIM MODULE #############");
		System.out.println("##################################################################################################################################");
	}
}
