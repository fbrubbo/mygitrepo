package br.com.datamaio.env.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.util.Properties;

public class EmbeddedGroovy {
	protected File groovyFile;
	protected Hook hook;
	
	public EmbeddedGroovy(String groovyPath, Properties props) {
		this.groovyFile = new File(groovyPath);			
		this.hook = newInstance();
		setProperties(props);
	}
	
	private Hook newInstance(){		
		if(exists()) {
			try {
				final ClassLoader parent = ModuleHookEmbeddedGroovy.class.getClassLoader();
				final GroovyClassLoader loader = new GroovyClassLoader(parent);
				final Class<?> groovyClass = loader.parseClass(this.groovyFile);
				
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
			this.hook.finish();
		}
	}
	
	protected boolean exists(){
		return this.groovyFile.exists();
	}
	
	private void setProperties(final Properties props) {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("props", props);
		}
	}	
}
