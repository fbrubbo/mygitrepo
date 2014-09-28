package br.com.datamaio.envconfig.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.logging.Logger;

import br.com.datamaio.fwk.io.ZipUtils;

public class WindowsCommand extends Command {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
		throw new RuntimeException("Operacao 'install' Nao implementado para windows");
	}

	public void uninstall(String pack) {
		throw new RuntimeException("Operacao 'install' Nao implementado para windows");
	}

	@Override
	public String chmod(String mode, String file) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String chmod(String mode, String file, boolean recursive) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public void dos2unix(String file) {
		// do nothing
	}

	@Override
	public String chown(String user, String file) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String chown(String user, String file, boolean recursive) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String chown(String user, String group, String file, boolean recursive) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}
	
	@Override
	public String ln(String file, String link) {
		throw new RuntimeException("Operacao nao suportada no windows");
	}

	@Override
	public String groupadd(String group) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String groupadd(String group, String options) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String useradd(String user) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String useradd(String user, String options) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String passwd(String user, String passwd) {
		throw new RuntimeException("Operacao Nao implementado para windows");
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
	
}
