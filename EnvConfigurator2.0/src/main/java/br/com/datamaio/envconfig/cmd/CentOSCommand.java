package br.com.datamaio.envconfig.cmd;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CentOSCommand extends LinuxCommand {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final String DIST_NAME = "CentOS";
	
	public void uninstall(String pack) {
		LOGGER.info("\t********** Removing package " + pack);
		run("yum -y erase " +pack );
	}
	
	@Override
	protected List<String> buildInstallCommand(String pack) {
		return Arrays.asList(new String[] { "yum", "-y", "install", pack });
	}
	
	@Override
	public void installFromLocalPath(String path) {
		LOGGER.info("Instalando RPM a partir de " + path + " ... ");
		run("rpm -i " + path);
		LOGGER.info("RPM instalado com sucesso! ");
	}
	
	@Override
	public String distribution() {
		return DIST_NAME;
	}
	
	@Override
	public void addRepository(String repository) {
		throw new RuntimeException("Not Implemented!");
	}
}
