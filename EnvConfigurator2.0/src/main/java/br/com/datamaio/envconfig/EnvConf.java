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

import br.com.datamaio.envconfig.groovy.ModuleHookEmbeddedGroovy;
import br.com.datamaio.envconfig.util.VariablePathUtils;
import br.com.datamaio.fwk.io.CopyVisitor;
import br.com.datamaio.fwk.io.DeleteVisitor;
import br.com.datamaio.fwk.io.FileUtils;

public class EnvConf {
	private static final String DELETE_SUFFIX = ".del";
	private static final String TEMPLATE_SUFFIX = ".tmpl";

	private static final Logger LOGGER = Logger .getLogger(EnvConf.class);

	private ExternalConf conf;
	private Path module;
	private final VariablePathUtils vpu;

	public EnvConf(Path config, Path module2Install) {
		this(new ExternalConf().load(config), module2Install);
	}

	public EnvConf(ExternalConf conf, Path module2Install) {
		this.conf = conf;
		this.module = module2Install;
		this.vpu = new VariablePathUtils(conf, module);
	}

	public void exec() {
		// TODO: testar
		try {
			final ModuleHookEmbeddedGroovy groovy = new ModuleHookEmbeddedGroovy(module.toString(), conf);
			LOGGER.info("Instalando Modulo " + module + "..");
			if (groovy.pre()) {
				deleteFiles();
				copyAndMergeFiles();
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
			@Override /** Verificar se o arquivo existe antes de tentar deletar */
			protected boolean mustDelete(Path path) {				
				return super.mustDelete(path) ? exists(vpu.getTargetWithoutSuffix(path, DELETE_SUFFIX)) : false;
			}
			@Override /** Deleta o target e não source */ 
			protected void delete(Path path) throws IOException {
				Path toDelete = vpu.getTargetWithoutSuffix(path, DELETE_SUFFIX);
				FileUtils.delete(toDelete);
				LOGGER.info("DELETED: " + toDelete);
			}
		});
	}
	
	protected void copyAndMergeFiles() {
		final Map<String, String> bindings = conf.entrySet().stream()
				.collect(toMap(e -> e.getKey().toString()
							  ,e -> e.getValue().toString()));
		final SimpleTemplateEngine engine = new SimpleTemplateEngine();
		final Path target = vpu.getTarget(module);
		
		FileUtils.copy(new CopyVisitor(module, target, "*" + DELETE_SUFFIX){
			@Override /** Não considera os .del */
			protected boolean mustCopy(Path file) {
				return !matcher.matches(file.getFileName());
			}
			@Override /** Copia OU faz o merge do template */
			protected void copy(Path fileToCopy, final Path resolvedTargetPath) throws IOException {
				if(fileToCopy.toString().endsWith(TEMPLATE_SUFFIX)) {
					File resolvedTargetFile = new File(resolvedTargetPath.toString().replace(TEMPLATE_SUFFIX, ""));
				    try (Writer out = new BufferedWriter(new FileWriter(resolvedTargetFile))) {
						Writable tmplt = engine.createTemplate(fileToCopy.toFile()).make(bindings);
						tmplt.writeTo(out);
					} catch (Exception e) {
						throw new IOException(e);
					}				
				    LOGGER.info("MERGED: " + resolvedTargetFile);
				} else {
					Files.copy(fileToCopy, resolvedTargetPath, REPLACE_EXISTING);
					LOGGER.info("COPIED: " + resolvedTargetPath);
				}
			}
		});
	}
}
