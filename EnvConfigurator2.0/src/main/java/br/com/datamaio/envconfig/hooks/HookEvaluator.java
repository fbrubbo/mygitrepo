package br.com.datamaio.envconfig.hooks;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.codehaus.groovy.control.CompilerConfiguration;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.fwk.io.FileUtils;

public abstract class HookEvaluator {
	private Path groovyPath;
	private GroovyShell shell;
	protected String script;
	
	public HookEvaluator(Path groovyPath, Map<String, Object> binds, Configuration conf) {
		this.groovyPath = groovyPath;
		
		binds = buildBinding(binds, conf);
		this.shell = createShell(binds, conf);
		this.script = readScript(binds);
	}

	private Map<String, Object> buildBinding(Map<String, Object> binds, Configuration conf) {
		binds.put("conf", conf);
		binds.put("envs", conf.getEnvironments());
		binds.put("props", conf.getProperties());
		return binds;
	}

	private String readScript(Map<String, Object> binds) {
		if( Files.exists(groovyPath) ) {
			String script = "";
			for (String b : binds.keySet()) {
				script += "set" + b.substring(0,1).toUpperCase() + b.substring(1) + "(" + b + ");\n";
			}
			return script + FileUtils.read(groovyPath);
		}
		
		return null;
	}

	public boolean pre(){
		if(exists()) {
			return (boolean) evaluate("pre");
		}
		
		return true;
	}
	
	public void post(){
		if(exists()) {
			evaluate("post");
		}
	}
	
	public void finish(){
		if(exists()) {
			evaluate("finish");
		}
	}
	
	private boolean exists() {
		return script!=null;
	}

	private Object evaluate(String action) {
		String fullScript = script + "\n " + action + "();";
		return shell.evaluate(fullScript);
	}
	
	private GroovyShell createShell(Map<String, Object> binds, Configuration conf) {
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setScriptBaseClass(getScriptBaseClass());
		Binding binding = new Binding();
		for (String b : binds.keySet()) {
			binding.setProperty(b, binds.get(b));
		}
		ClassLoader loader = this.getClass().getClassLoader();
		return new GroovyShell(loader, binding, configuration);
	}

	protected abstract String getScriptBaseClass();
	
	@Override
	public String toString() {
		return groovyPath.toString();
	}
}
