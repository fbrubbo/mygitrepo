package br.com.datamaio.envconfig.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;

import br.com.datamaio.envconfig.conf.Configuration;

public class EmbeddedGroovy {
	protected File groovyFile;
	protected Hook hook;
	
	public EmbeddedGroovy(String groovyPath, Configuration conf) {
		this.groovyFile = new File(groovyPath);			
		this.hook = newInstance();
		setConfiguration(conf);
	}
	
	private Hook newInstance(){		
		if(exists()) {
			try {
				final ClassLoader parent = ModuleHookEmbeddedGroovy.class.getClassLoader();
				final GroovyClassLoader loader = new GroovyClassLoader(parent);
				final Class<?> groovyClass = loader.parseClass(this.groovyFile);
				loader.close();
				
				final GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();				
				return (Hook) groovyObject;
			} catch (Exception e) {
				throw new RuntimeException("Ocorreu um erro criando objeto do groovy", e);
			}
		}
		
		return null;
	}	
	
	public boolean pre(){
		if(exists()) {
			return this.hook.pre();
		}
		
		return true;
	}
	
	public void post(){
		if(exists()) {
			this.hook.post();
		}
	}
	
	public void finish(){
		if(exists()) {
			this.hook.finish();
		}
	}
	
	protected boolean exists(){
		return this.groovyFile.exists();
	}
	
	private void setConfiguration(final Configuration conf) {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("envs", conf.getEnvironments());
			groovyObject.setProperty("props", conf.getInstalationProperties());
			groovyObject.setProperty("conf", conf);
		}
	}	
}
