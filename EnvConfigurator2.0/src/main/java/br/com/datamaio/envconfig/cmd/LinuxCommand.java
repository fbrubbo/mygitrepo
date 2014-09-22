package br.com.datamaio.envconfig.cmd;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public abstract class LinuxCommand extends Command {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public String chmod(String mode, String file) {
		return chmod(mode, file, false);
	}

	public String chmod(String mode, String file, boolean recursive) {
		List<String> cmd = new ArrayList<String>();
		cmd.add("chmod");
		if (recursive) {
			cmd.add("-R");
		}
		cmd.add(mode);
		cmd.add(file);
		String ret = run(cmd);

		if (file.endsWith(".sh")) {
			// executa este cara apenas para garantir que se alguem salvou no windows
			// este arquivo possa ser executado no Linux
			dos2unix(file);
		}

		return ret;
	}

	public void dos2unix(String file) {
		if(!Files.exists(Paths.get("/usr/bin/dos2unix"))) {
			install("dos2unix");
		}
		
		List<String> cmd = new ArrayList<String>();
		cmd.add("dos2unix");
		cmd.add(file);
		run(cmd);
	}

	public String chown(String user, String file) {
		return chown(user, file, false);
	}

	public String chown(String user, String file, boolean recursive) {
		return chown(user, user, file, recursive);
	}

	public String chown(String user, String group, String file, boolean recursive) {
		List<String> cmd = new ArrayList<String>();
		cmd.add("chown");
		if (recursive) {
			cmd.add("-R");
		}
		cmd.add(user + ":" + group);
		cmd.add(file);
		return run(cmd);
	}

	public String mkdir(String dir) {
		return run("mkdir -p " + dir);
	}

	public String mv(String from, String to) {
		List<String> cmd = new ArrayList<String>();
		cmd.add("/bin/mv");
		cmd.add("-f");
		cmd.add(from);
		cmd.add(to);
		return run(cmd);
	}

	public String ls(String path) {
		return run("ls " + path);
	}

	public String ln(final String file, final String link) {
		return run("ln -sf " + file + " " + link);
	}

	public String rm(final String path) {
		return run("/bin/rm -f " + path);
	}

	public String rm(final String path, final boolean isRecursive) {
		return run("/bin/rm -f" + (isRecursive ? "R" : "") + " " + path);
	}

	public String cp(final String from, final String to) {
		return run("/bin/cp -f " + from + " " + to);
	}

	public String groupadd(final String group) {
		return groupadd(group, null);
	}

	public String groupadd(final String group, final String options) {
		return run("groupadd " + (options != null ? options : "-f") + " " + group);
	}

	public String useradd(final String user) {
		return useradd(user, null);
	}

	public String useradd(final String user, final String options) {
		try {
			String ret = run("id " + user, false);
			LOGGER.info("Usuario ja existe. Nao sera criado novamente.");
			return ret;
		} catch (Exception e) {
			LOGGER.info("Usuario nao existe. Criando ...");
			return run("useradd " + (options != null ? options : "") + " " + user);
		}
	}

	/**
	 * OBS IMPORTANTE: Se o selinux estiver ligado este método não funciona.
	 */
	public String passwd(final String user, final String passwd) {
		List<String> cmd = Arrays.asList("passwd", user);
		return run(cmd, new Interact() {
			@Override
			void execute(OutputStream out) throws Exception {
				byte[] bytes = (passwd + "\n").getBytes();
				out.write(bytes); // entra a senha
				out.write(bytes); // confirma a senha
			}
		});
	}
	
	public void unzip(String from, String toDir) {
		LOGGER.info("Descompactando " + from + " para " + toDir + " ... ");
		run("unzip -o " + from + " -d " + toDir);
		LOGGER.info("Descompatacao concluida!");
	}
	

	// ----------- begin bash run --------

	/**
	 * Commando bash foi criado pois alguns comandos não são compreendidos
	 * corretamente pelo linux quando executados pelo java.
	 *
	 * Por exemplo o comando: echo '777777' > ~/test Quando executado
	 * diretamente no linux ele coloca o texto '777777' dentro do arquivo test
	 * Quando executado pelo java ele imprime no output '777777' > ~/test
	 *
	 * Forma de usar: bash("echo '777777' > ~/test")
	 */
	public String bash(String cmd) {
		return bash(cmd, true);
	}

	public String bash(String cmd, final boolean printOutput) {
		return run("bash -c " + cmd, printOutput);
	}

	public String bash(String cmd, final int... successfulExec) {
		return run("bash -c " + cmd, successfulExec);
	}

	public String bashWithNoInteraction(String cmd) {
		return run("bash -c " + cmd, (Interact) null);
	}

	// ---------- end bash run -------

	
	public void install(String pack) {
		LOGGER.info("\t********** Instalando pacote " + pack);
		run(buildInstallCommand(pack));
		LOGGER.info("\t********** Pacote " + pack + " instalado\n");
	}

	protected abstract List<String> buildInstallCommand(String pack);

	
	public void uninstall(String pack) {
		LOGGER.info("\t********** Removendo pacote " + pack);
		List<String> cmd = buildUnistallCommand(pack);
		run(cmd, new Interact());
		LOGGER.info("\t********** Pacote " + pack + " removido\n");
	}

	protected abstract List<String> buildUnistallCommand(String pack);

}
