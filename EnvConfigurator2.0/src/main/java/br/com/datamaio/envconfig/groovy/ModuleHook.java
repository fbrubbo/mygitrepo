package br.com.datamaio.envconfig.groovy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.Constants;
import br.com.datamaio.envconfig.util.Cmd;
import br.com.datamaio.fwk.io.FileUtils;
import br.com.datamaio.fwk.io.ZipUtils;

public class ModuleHook extends Hook {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static final String ROOT = "root";

	protected void requestSudoPassword() {
		if (ROOT.equals(whoami())) {
			LOGGER.info(">> Eu sou ROOT, sendo assim não preciso de SUDO!");
		} else {
			System.setProperty(Constants.LINUX_USE_SUDO, "true");
			LOGGER.info("Entre com o password para executar os proximo comandos com sudo (OBS. o comando abaixo serve apenas para testar o sudo):");
			final String result = Cmd.run("ls /root/", false);

			// o if abaixo é apenas por garantia. Vai que não dê erro e apenas
			// aparece permissão negada no output
			if (result == null || result.contains("Permission denied") || result.contains("PermissÃ£o negada")
					|| result.contains("Permissão negada") || result.contains("não permitida") || result.contains("nÃ£o permitida")) {
				throw new RuntimeException("Permissão negada!");
			}
		}
	}

	protected void doNotUseSudoAnyMore() {
		System.setProperty(Constants.LINUX_USE_SUDO, "false");
	}

	public void unzip(String depName, String toDir) {
		Path from = resolveDependency(depName);
		log("Descompactando " + from + " para " + toDir + " ... ");
		if (isLinux()) {
			log("Usando comando nativo do linux");
			Cmd.run("unzip -o " + from + " -d " + toDir);
		} else {
			log("Usando API interna");
			ZipUtils.unzip(from, Paths.get(toDir));
		}
		log("Descompatacao concluida!");
	}

	public void rpm(String depName) {
		Path from = resolveDependency(depName);
		log("Instalando RPM a partir de " + from + " ... ");
		if (isLinux()) {
			run("rpm -i " + from);
		} else {
			log("RPM nao executado pois nao eh linux RH ..");
		}
	}

	public void deployPackages(Path deploymentPath) {
		String baseDir = null;
		try {
			baseDir = new java.io.File(".").getCanonicalFile().toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Path packsToInstall = Paths.get(baseDir, "packsToDeploy", "bin");

		log("-------------------------------------------------------------------------------------------------------------------------------------");
		log("- Instalando pacotes WAR definidos em 'packsToDeploy/bin/' no diretorio '$deploymentPath' ...");
		log("-------------------------------------------------------------------------------------------------------------------------------------");
		FileUtils.copy(packsToInstall, deploymentPath, "*.war");

		log("-------------------------------------------------------------------------------------------------------------------------------------");
		log("- Instalando pacotes EAR definidos em 'packsToDeploy/bin/' no diretorio '$deploymentPath' ...");
		log("-------------------------------------------------------------------------------------------------------------------------------------");
		FileUtils.copy(packsToInstall, deploymentPath, "*.ear");
	}
}