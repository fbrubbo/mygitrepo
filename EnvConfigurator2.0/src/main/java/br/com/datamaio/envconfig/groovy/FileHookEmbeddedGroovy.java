package br.com.datamaio.envconfig.groovy;

import groovy.lang.GroovyObject;

import java.io.File;
import java.nio.file.Path;

import br.com.datamaio.envconfig.conf.Configuration;

public class FileHookEmbeddedGroovy extends EmbeddedGroovy {
	
	public FileHookEmbeddedGroovy(final Path src, final Path target, final Configuration conf) {
		this(src.toFile(), target.toFile(), conf);
	}
	
	public FileHookEmbeddedGroovy(final File src, final File target, final Configuration conf) {
		super(src.getAbsolutePath() + Configuration.HOOK_SUFFIX, conf);
		setSrc(src);
		setTarget(target);
	}
	
	private void setSrc(final File src) {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("srcPath", src.getAbsolutePath());
			groovyObject.setProperty("srcFile", src);
		}
	}	

	private void setTarget(final File target) {
		if(hook!=null) {
			final GroovyObject groovyObject = (GroovyObject) hook;
			groovyObject.setProperty("targetPath", target.getAbsolutePath());
			groovyObject.setProperty("targetFile", target);
		}
	}	
}
