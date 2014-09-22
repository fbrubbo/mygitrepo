package br.com.datamaio.envconfig.cmd;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class UbuntuCommand extends LinuxCommand {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final String DIST_NAME = "Ubuntu";
			
	protected List<String> buildInstallCommand(String pack) {
		return Arrays.asList(new String[] { "apt-get", "install", pack });
	}
	protected List<String> buildUnistallCommand(String pack) {
		return Arrays.asList(new String[] { "apt-get", "erase", pack });
	}
	public void installLocalPack(String path) {
		LOGGER.info("Instalando DEB File a partir de " + path + " ... ");
		run("dpkg -i " + path);
		LOGGER.info("DEB File instalado com sucesso! ");
	}
}
