package br.com.datamaio.envconfig;

import static java.nio.file.Files.exists;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.stream.Collectors.toMap;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.datamaio.envconfig.groovy.FileHookEmbeddedGroovy;
import br.com.datamaio.envconfig.groovy.ModuleHookEmbeddedGroovy;
import br.com.datamaio.envconfig.util.VariablePathHelper;
import br.com.datamaio.fwk.io.CopyVisitor;
import br.com.datamaio.fwk.io.DeleteVisitor;
import br.com.datamaio.fwk.io.FileUtils;

/**
 * OBS: Esta classe não é thread safe devido aos FileHookEmbeddedGroovy. 
 * Se precisar modificar, basta sempre instanciar eles na hora de usar. Ai a classe vira thread safe
 * 
 * @author Fernando Rubbo
 */
public class EnvConf {
	private static final Logger LOGGER = Logger .getLogger(EnvConf.class);
	
	private static final String HOOK_SUFFIX = ".hook";
	private static final String DELETE_SUFFIX = ".del";
	private static final String TEMPLATE_SUFFIX = ".tmpl";

	private ExternalConf conf;
	private Path module;
	private final VariablePathHelper pathHelper;

	public EnvConf(Path config, Path module2Install) {
		this(new ExternalConf().load(config), module2Install);
	}

	public EnvConf(ExternalConf conf, Path module2Install) {
		this.conf = conf;
		this.module = module2Install;
		this.pathHelper = new VariablePathHelper(conf, module);
	}

	public void exec() {
		try {
			final ModuleHookEmbeddedGroovy groovy = new ModuleHookEmbeddedGroovy(HOOK_SUFFIX, module, conf);
			LOGGER.info("Instalando Modulo " + module + "..");
			if (groovy.pre()) {
				deleteFiles();
				copyFiles();
				groovy.post();
			} else {
				LOGGER.info("Modulo " + module + " nao foi instalado neste ambiente pois o Hook.groovy retornou false");
			}
		} catch (final Exception e) {
			LOGGER.error(e);
			throw new RuntimeException("Erro inesperado. Causa: " + e.getMessage(), e);
		}
	}

	protected void deleteFiles() {				
		FileUtils.deleteDir(module, new DeleteVisitor("*" + DELETE_SUFFIX){
			private FileHookEmbeddedGroovy hook;
			
			@Override /** Verificar se o arquivo existe antes de tentar deletar */
			protected boolean mustDelete(Path source) {
				if(source.toString().endsWith(HOOK_SUFFIX)){
					return false;
				}
				
				final Path target = pathHelper.getTargetWithoutSuffix(source, DELETE_SUFFIX);
				hook = new FileHookEmbeddedGroovy(HOOK_SUFFIX, source, target, conf);  
				return super.mustDelete(source) 
						&& exists(target) 
						&& hook.pre();
			}
			
			@Override /** Deleta o target e não source */ 
			protected void delete(Path source) throws IOException {
				Path target = pathHelper.getTargetWithoutSuffix(source, DELETE_SUFFIX);
				FileUtils.delete(target);
				hook.post();
				LOGGER.info("DELETED: " + target);
			}
		});
	}
	
	protected void copyFiles() {
		final Map<String, String> bindings = conf.entrySet().stream()
				.collect(toMap(e -> e.getKey().toString()
							  ,e -> e.getValue().toString()));
		final SimpleTemplateEngine engine = new SimpleTemplateEngine();
		final Path target = pathHelper.getTarget(module);
		
		FileUtils.copy(new CopyVisitor(module, target, "*" + DELETE_SUFFIX){
			private FileHookEmbeddedGroovy hook;
			
			@Override /** Não considera os .del */
			protected boolean mustCopy(Path source) {
				if(source.toString().endsWith(HOOK_SUFFIX)){
					return false;
				}
				
				final Path target = pathHelper.getTargetWithoutSuffix(source, TEMPLATE_SUFFIX);
				hook = new FileHookEmbeddedGroovy(HOOK_SUFFIX, source, target, conf);  
				return !matcher.matches(source.getFileName())
						&& hook.pre();
			}
			
			@Override /** Copia OU faz o merge do template */
			protected void copy(Path source, final Path target) throws IOException {
				if(source.toString().endsWith(TEMPLATE_SUFFIX)) {
					File resolvedTargetFile = new File(target.toString().replace(TEMPLATE_SUFFIX, ""));
				    try (Writer out = new BufferedWriter(new FileWriter(resolvedTargetFile))) {
						Writable tmplt = engine.createTemplate(source.toFile()).make(bindings);
						tmplt.writeTo(out);
					} catch (Exception e) {
						throw new IOException(e);
					}
				    hook.post();
				    LOGGER.info("MERGED: " + resolvedTargetFile);
				} else {
					Files.copy(source, target, REPLACE_EXISTING);
					hook.post();
					LOGGER.info("COPIED: " + target);
				}				
			}
		});
	}
}
