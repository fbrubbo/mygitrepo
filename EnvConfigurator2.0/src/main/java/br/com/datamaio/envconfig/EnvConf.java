package br.com.datamaio.envconfig;

import static java.nio.file.Files.exists;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import br.com.datamaio.envconfig.util.VariablePathUtils;
import br.com.datamaio.fwk.io.DeleteVisitor;
import br.com.datamaio.fwk.io.FileUtils;

public class EnvConf {
	private static final String DELETE_SUFFIX = ".del";

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
	
	private void copyAndMergeFiles() {
		// TODO Auto-generated method stub
		
	}


}
