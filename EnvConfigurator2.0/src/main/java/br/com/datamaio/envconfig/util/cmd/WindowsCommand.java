package br.com.datamaio.envconfig.util.cmd;

import java.nio.file.Paths;
import java.util.logging.Logger;

import br.com.datamaio.fwk.io.ZipUtils;

public class WindowsCommand extends Command {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
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
		throw new RuntimeException("Operacao Nao implementado para windows");

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
	public String mkdir(String dir) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String mv(String from, String to) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String ls(String path) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String ln(String file, String link) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String rm(String path) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String rm(String path, boolean isRecursive) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}

	@Override
	public String cp(String from, String to) {
		throw new RuntimeException("Operacao Nao implementado para windows");
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
	public void installLocalPack(String path) {
		throw new RuntimeException("Operacao Nao implementado para windows");
	}
}
