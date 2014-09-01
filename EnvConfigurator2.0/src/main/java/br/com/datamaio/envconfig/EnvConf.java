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

	public EnvConf(Path config, Path module2Install) {
		this.conf = new ExternalConf();
		this.conf.load(config);
		this.module = module2Install;
	}

	public EnvConf(ExternalConf conf, Path module2Install) {
		this.conf = conf;
		this.module = module2Install;
	}

	public void exec() {
		
//		try {
//			final ModuleHookEmbeddedGroovy groovy = new ModuleHookEmbeddedGroovy(modulePath, props);
//			if (groovy.pre()) {
//				LOGGER.info("Instalando Modulo " + module);
//				deleteFiles();
//				copyAndMergeFiles();
//				groovy.post();
//			} else {
//				LOGGER.info("Modulo " + module + " nao foi instalado neste ambiente pois o Hook.groovy retornou false");
//			}
//		} catch (final Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Erro inesperado. Causa: "
//					+ e.getMessage(), e);
//		}
	}

	protected void deleteFiles() {
 
		/** Este visitor navega no module e, caso precise apagar, tenta apagar do target -- caminho que o módulo está apontando */
		DeleteVisitor visitor = new DeleteVisitor("*" + DELETE_SUFFIX){
			VariablePathUtils vpu = new VariablePathUtils(conf, module);
			
			@Override
			protected boolean mustDelete(Path path) {				
				return super.mustDelete(path) ? exists(vpu.getTargetWithoutSuffix(path, DELETE_SUFFIX)) : false;
			}
			
			/** sobreescrito para deletar o target e não apenas o path sendo navegado */ 
			protected void delete(Path path) throws IOException {
				Path toDelete = vpu.getTargetWithoutSuffix(path, DELETE_SUFFIX);
				FileUtils.delete(toDelete);
				LOGGER.info("DELETED: " + toDelete);
			}
		};
		
		FileUtils.deleteDir(module, visitor);
	}
	
	protected void copyAndMergeFiles() {
		final Map<String, String> bindings = conf.entrySet().stream()
				.collect(toMap(e -> e.getKey().toString()
							  ,e -> e.getValue().toString()));
		final SimpleTemplateEngine engine = new SimpleTemplateEngine();
		
		VariablePathUtils vpu = new VariablePathUtils(conf, module);
		Path target = vpu.getTarget(module);
		FileUtils.copy(new CopyVisitor(module, target, "*" + DELETE_SUFFIX){
			protected boolean mustCopy(Path file) {
				return !matcher.matches(file.getFileName());
			}

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
					LOGGER.info("COPYED: " + resolvedTargetPath);
				}
			}
		});
		
	}
}
