package br.com.datamaio.envconfig.cmd;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class UbuntuCommand extends LinuxCommand {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final String DIST_NAME = "Ubuntu";
	
	public void uninstall(String pack) {
		LOGGER.info("\t********** Removing package " + pack);
		run( "apt-get -y purge " + pack);
		run("apt-get -y autoremove");
	}
	
	@Override
	protected List<String> buildInstallCommand(String pack) {
		return Arrays.asList(new String[] { "apt-get", "-y", "install", pack });
	}
	
	@Override	
	public void installFromLocalPath(String path) {
		LOGGER.info("Installing DEB File from " + path + " ... ");
		run("dpkg -i " + path);
		LOGGER.info("DEB File successfully installed! ");
	}
	
	@Override
	public String distribution() {
		return DIST_NAME;
	}

	@Override
	public void addRepository(String repository) {
		run("add-apt-repository -y " + repository);
		run("apt-get update");
	}
}
