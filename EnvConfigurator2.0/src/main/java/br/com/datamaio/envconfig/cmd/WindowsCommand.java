package br.com.datamaio.envconfig.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.logging.Logger;

import br.com.datamaio.fwk.io.ZipUtils;

public class WindowsCommand extends Command {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	
	public void service(String name, ServiceAction action){
		throw new RuntimeException("Function 'service' not implemented for windows");
	}


	@Override
	public void execute(String file) {
		run(file);
	}
	
	@Override
	public String distribution() {
		String OS_NAME = "OS Name:";
		try {
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec("SYSTEMINFO");
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line = "";
			while ((line = in.readLine()) != null) {
				if (line.contains(OS_NAME)) {
					return line.substring(line.lastIndexOf(OS_NAME) + OS_NAME.length(), line.length() - 1);
				}
			}

			return "N/A";
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public void install(String pack) {
		run(pack);
	}

	public void uninstall(String pack) {
		throw new RuntimeException("Function 'uninstall' not implemented for windows");
	}

	@Override
	public void chmod(String mode, String file) {
		// do nothing
	}

	@Override
	public void chmod(String mode, String file, boolean recursive) {
		// do nothing
	}

	@Override
	public void dos2unix(String file) {
		// do nothing
	}

	@Override
	public void chown(String user, String file) {
		// do nothing
	}

	@Override
	public void chown(String user, String file, boolean recursive) {
		// do nothing
	}

	@Override
	public void chown(String user, String group, String file, boolean recursive) {
		// do nothing
	}
	
	@Override
	public void ln(String file, String link) {
		// do nothing
	}

	@Override
	public void groupadd(String group) {
		// do nothing
	}

	@Override
	public void groupadd(String group, String options) {
		// do nothing
	}

	@Override
	public void useradd(String user) {
		// do nothing
	}

	@Override
	public void useradd(String user, String options) {
		// do nothing
	}

	@Override
	public void passwd(String user, String passwd) {
		// do nothing
	}

	@Override
	public void unzip(String from, String toDir) {
		LOGGER.info("Descompactando " + from + " para " + toDir + " ... ");
		ZipUtils.unzip(Paths.get(from), Paths.get(toDir));
		LOGGER.info("Descompatacao concluida!");
	}

	@Override
	public void installFromLocalPath(String path) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}
	
	@Override
	public void addRepository(String repository) {
		throw new RuntimeException("Not Implemented!");
	}
}
