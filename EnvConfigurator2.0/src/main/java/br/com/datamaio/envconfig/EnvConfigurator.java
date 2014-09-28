package br.com.datamaio.envconfig;

import static br.com.datamaio.envconfig.conf.Configuration.DELETE_SUFFIX;
import static br.com.datamaio.envconfig.conf.Configuration.HOOK_SUFFIX;
import static br.com.datamaio.envconfig.conf.Configuration.TEMPLATE_SUFFIX;
import static java.nio.file.Files.exists;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import groovy.text.SimpleTemplateEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.ConfEnvironments;
import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.hooks.file.FileHookEvaluator;
import br.com.datamaio.envconfig.hooks.module.ModuleHookEvaluator;
import br.com.datamaio.envconfig.util.BackupHelper;
import br.com.datamaio.envconfig.util.LogHelper;
import br.com.datamaio.envconfig.util.PathHelper;
import br.com.datamaio.fwk.io.CopyVisitor;
import br.com.datamaio.fwk.io.DeleteVisitor;
import br.com.datamaio.fwk.io.FileUtils;

/**
 * OBS: Esta classe não é thread safe devido aos FileHookEmbeddedGroovy. 
 * Se precisar modificar, basta sempre instanciar eles na hora de usar. Ai a classe vira thread safe
 * 
 * @author Fernando Rubbo
 */
public class EnvConfigurator {
	// TODO: WindowsCommand dar erro quando executa comando linux específico ou ele passa reto. Tipo chmod
	// TODO: falta fazer o backup
	// TODO: testes para tudo. testar todas as possibilidades de backup
	// TODO: revisar tudo. rever todos TODOS
	// TODO: usar aqui o EncodingHelper para copiar, mergear os arquivos
	// TODO: multi language e doc em inglês
	// TODO: permitir lista de modulos. cuidado. impacta validacao do gradle
	/*
	 *  TODO(s):
	 *  - Gradle
	 *  	3) config do eclipse para ficar bonitinho
	 *  - Outros
	 *  	1) comparar FWKUtils ultima versão com a minha.. arrumar método a método. Melhorar testes
	 *
	 */
	
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private Configuration conf;
	private final PathHelper pathHelper;
	private final BackupHelper backupHelper;

	public EnvConfigurator(Path properties, Path module2Install) {
		this(properties, module2Install, new ConfEnvironments(), new HashMap<>());
	}
	
	EnvConfigurator(Map<String, String> instalationProperties, Path module2Install) {
		this(new Configuration(Paths.get(new File(".").getAbsolutePath(), "config"), instalationProperties, module2Install));
	}
	
	public EnvConfigurator(Path properties, Path module2Install, ConfEnvironments environments, Map<String, Path> dependencies) {
		this(new Configuration(properties, module2Install, environments, dependencies));
	}
	
	public EnvConfigurator(Configuration conf) {
		this.conf = conf;
		this.pathHelper = new PathHelper(conf);
		this.backupHelper = new BackupHelper(conf);
		new LogHelper(conf).startup();
	}

	public void exec() {
		conf.printProperties();
		Path module = conf.getModuleDir();
		try {			
			final ModuleHookEvaluator hook = new ModuleHookEvaluator(conf);
			try{
				if (hook.pre()) {
					deleteFiles();
					copyFiles();
					hook.post();
				} else {
					LOGGER.warning("Modulo " + module + " nao foi instalado neste ambiente pois o Module.hook retornou false");
				}
			} finally {
				hook.finish();
			}
		} catch (final Exception e) {
			LOGGER.log(Level.SEVERE, "Erro inesperado.", e);
			throw new RuntimeException("Erro inesperado. Causa: " + e.getMessage(), e);
		}
	}

	protected void deleteFiles() {		
		Path module = conf.getModuleDir();
		
		FileUtils.deleteDir(module, new DeleteVisitor("*" + DELETE_SUFFIX){
			private FileHookEvaluator hook;
			
			@Override /** Verificar se o arquivo existe antes de tentar deletar */
			protected boolean mustDelete(Path source) {
				if(source.toString().endsWith(HOOK_SUFFIX)){
					return false;
				}
				
				final Path target = pathHelper.getTargetWithoutSuffix(source, DELETE_SUFFIX);
				hook = new FileHookEvaluator(source, target, conf);
				return super.mustDelete(source) 
						&& exists(target) 
						&& hook.pre();
			}
			
			@Override /** Deleta o target e não source */ 
			protected void delete(Path source) throws IOException {
				try {
					Path target = pathHelper.getTargetWithoutSuffix(source, DELETE_SUFFIX);
					backupHelper.backupFileOrDir(target);
					FileUtils.delete(target);
					LOGGER.info(" :DELETED");
					LOGGER.info("\t" + target);
					hook.post();
				} finally {
					hook.finish();
				}
			}
		});
	}
	
	protected void copyFiles() {
		Path module = conf.getModuleDir();
		final Map<String, String> properties = conf.getProperties();
		
		final SimpleTemplateEngine engine = new SimpleTemplateEngine();
		final Path target = pathHelper.getTarget(module);
		
		FileUtils.copy(new CopyVisitor(module, target, "*" + DELETE_SUFFIX){
			private FileHookEvaluator hook;
			
			@Override /** Não considera os .del */
			protected boolean mustCopy(Path source) {
				if(source.toString().endsWith(HOOK_SUFFIX)){
					return false;
				}
				
				final Path target = pathHelper.getTargetWithoutSuffix(source, TEMPLATE_SUFFIX);
				hook = new FileHookEvaluator(source, target, conf);
				boolean pre = hook.pre();
				return !matcher.matches(source.getFileName()) && pre;
			}
			
			@Override /** Copia OU faz o merge do template */
			protected void copy(Path source, final Path target) throws IOException {
				try {
					if(source.toString().endsWith(TEMPLATE_SUFFIX)) {
						File resolvedTargetFile = new File(target.toString().replace(TEMPLATE_SUFFIX, ""));
						backupHelper.backupFile(resolvedTargetFile.toPath());
					    try (Writer out = new BufferedWriter(new FileWriter(resolvedTargetFile))) {
							engine.createTemplate(source.toFile())
								.make(properties)
								.writeTo(out);
						} catch (Exception e) {
							throw new IOException(e);
						}
					    LOGGER.info(" :MERGED");
					    LOGGER.info("\t" + source + " -> " + resolvedTargetFile);
					    hook.post();
					} else {
						backupHelper.backupFile(target);
						Files.copy(source, target, REPLACE_EXISTING);
						LOGGER.info(" :COPIED");
						LOGGER.info("\t" + source + " -> " + target);
						hook.post();
					}
				} finally {
					hook.finish();
				}
			}
		});
	}
}
