package br.com.datamaio.env;

import static java.nio.file.Files.exists;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import br.com.datamaio.env.groovy.ModuleHookEmbeddedGroovy;
import br.com.datamaio.env.util.VariablePathUtils;
import br.com.datamaio.fwk.io.DeleteVisitor;
import br.com.datamaio.fwk.io.FileUtils;
import br.com.datamaio.fwk.io.PathUtils;

public class EnvConfigurator {
	private static final Logger LOGGER = Logger
			.getLogger(EnvConfigurator.class);

	private ExternalConf conf;
	private Path module;

	public EnvConfigurator(Path conf, Path module) {
		this.conf = new ExternalConf();
		this.conf.load(conf);
		this.module = module;
	}

	public EnvConfigurator(ExternalConf conf, Path module) {
		this.conf = conf;
		this.module = module;
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

	private void deleteFiles() {
 
		// TODO: TESTAR
		/** Este visitor navega no module e, caso precise apagar, tenta apagar do target -- caminho que o módulo está apontando */
		DeleteVisitor visitor = new DeleteVisitor("*.delete"){
			VariablePathUtils vpu = new VariablePathUtils(conf, module);
			
			// TODO: testar e revisar logs.. uma vez que os logs irão ficar estranhos no caso que existir o arquivo.txt.delete no module e o arquivo.txt já tenha sido deletado
			// talvez este must delete abaixo resolva o probelma
			@Override
			protected boolean mustDelete(Path path) {				
				return super.mustDelete(path) ? exists(vpu.getTargetWithoutSuffix(path, ".delete")) : false;
			}
			
			/** sobreescrito para deletar o target e não apenas o path sendo navegado */ 
			protected void delete(Path path) throws IOException {
				Path toDelete = vpu.getTargetWithoutSuffix(path, ".delete");
				Files.deleteIfExists(toDelete);
			}
		};
		
		FileUtils.deleteDir(module, visitor);
	}
	
	private void copyAndMergeFiles() {
		// TODO Auto-generated method stub
		
	}


}
