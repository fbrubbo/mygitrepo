package br.com.datamaio.envconfig.util.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public abstract class Command {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static class Interact {
		boolean printOutput() {
			return true;
		}
		void execute(OutputStream out) throws Exception {
			// do nothing
		}
		boolean isExecutionSuccessful(int waitfor) {
			return 0 == waitfor;
		}
	}
	
	public static final String OS_LINUX = "Linux";
	public static final String OS_WINDOWS = "Windows";

	public static String osname() {
		return System.getProperty("os.name");
	}

	public static boolean isLinux() {
		String os = osname();
		return os != null ? os.toUpperCase().contains(OS_LINUX.toUpperCase()) : false;
	}

	public String whoami() {
		return System.getProperty("user.name");
	}
	
	public static final Command get() {
		if(isLinux()) {
			List<String> cmdList = Arrays.asList("uname -a".split(" "));
			return new UbuntuCommand();
			
//			String dist = _run(cmdList, new Interact());
//			if(dist.contains(UbuntuCommand.DIST_NAME)) {
//			
//			} else if (dist.contains(CentOSCommand.DIST_NAME)) {
//				return new CentOSCommand();
//			} else {
//				throw new RuntimeException("Distribuicao de Linux nao suportada : " + dist);
//			}
		}
		return new WindowsCommand();
	}
	
	public abstract void install(String pack);
	public abstract void installLocalPack(String path);
	public abstract void uninstall(String pack);
	public abstract void unzip(String from, String toDir);
		
	public abstract void dos2unix(String file);
		
	public abstract String mkdir(String dir);
	public abstract String mv(String from, String to);
	public abstract String ls(String path);
	public abstract String ln(final String file, final String link);
	public abstract String rm(final String path);
	public abstract String rm(final String path, final boolean isRecursive);
	public abstract String cp(final String from, final String to);
	
	public abstract String groupadd(final String group);
	public abstract String groupadd(final String group, final String options);
	public abstract String useradd(final String user);
	public abstract String useradd(final String user, final String options);
	public abstract String passwd(final String user, final String passwd);
	
	public abstract String chmod(String mode, String file);
	public abstract String chmod(String mode, String file, boolean recursive);
	public abstract String chown(String user, String file);
	public abstract String chown(String user, String file, boolean recursive);
	public abstract String chown(String user, String group, String file, boolean recursive);
	
	public String run(String cmd) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList);
	}
	
	public String run(List<String> cmdList) {
		return run(cmdList, true);
	}

	public String run(String cmd, final boolean printOutput) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, printOutput);
	}
	
	public String run(List<String> cmdList, final boolean printOutput) {
		return run(cmdList, new Interact() {
			@Override
			boolean printOutput() {
				return printOutput;
			}
		});
	}

	public String run(String cmd, final int... successfulExec) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, successfulExec);
	}
	
	public String run(List<String> cmdList, final int... successfulExec) {
		return run(cmdList, new Interact() {
			@Override
			boolean isExecutionSuccessful(int waitfor) {
				return Arrays.binarySearch(successfulExec, waitfor) != -1;
			}
		});
	}

	/**
	 * Este metodo nao faz interacao nenhuma. Isto é, ele não mostra o output e
	 * nem o erro. Mas se o retorno do comando for diferente de 0, ele continua
	 * lancando uma exception
	 *
	 * OBS> este metodo foi criado pois alguns executaveis travavam lendo o
	 * output
	 */
	public String runWithNoInteraction(String cmd) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return runWithNoInteraction(cmdList);
	}
		
	public String runWithNoInteraction(List<String> cmdList) {
		return run(cmdList, (Interact) null);
	}
	


	public String run(String cmd, Interact interact) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, interact);
	}
	
	public String run(List<String> cmdList, Interact interact) { 
		return _run(cmdList, interact);
	}
	
	private static String _run(List<String> cmd, Interact interact) {
		ThreadedStreamHandler handler = null;
		ThreadedStreamHandler errorHandler = null;
		OutputStream out = null;
		Path tempPath = null;
		StringBuilder noInteractionHandler = new StringBuilder();
		try {
			if (interact == null) {
				// se não tem iteração joga o conteúdo no arquivo para
				// depois ler
				tempPath = Files.createTempFile("cmd", ".out");
			}

			final Process process = run(cmd, tempPath);
			if (interact != null) {
				InputStream in = process.getInputStream();
				InputStream ein = process.getErrorStream();
				out = process.getOutputStream();

				handler = new ThreadedStreamHandler(in, interact.printOutput());
				errorHandler = new ThreadedStreamHandler(ein, interact.printOutput());
				handler.start();
				errorHandler.start();

				// se o usuario definiu algum tipo de interacao
				interact.execute(out);
				out.flush();
			}
			int waitFor = process.waitFor();

			if (interact != null) {
				handler.interrupt();
				errorHandler.interrupt();
				handler.join();
				errorHandler.join();
			} else {
				// se não teve interação, imprime todo o stdout aqui após
				// finalizar o processo
				// isto é necessário para o usuário saber o que está
				// acontecendo
				for (String line : Files.readAllLines(tempPath, Charset.defaultCharset())) {
					LOGGER.info("\t\t" + line);
					noInteractionHandler.append(line).append("\n");
				}
			}

			if (interact != null) {
				if (!interact.isExecutionSuccessful(waitFor)) {
					throwExecutionException(errorHandler, waitFor);
				}
			} else if (waitFor != 0) {
				throwExecutionException(errorHandler, waitFor);
			}
			return (interact != null) ? handler.getOutput() : noInteractionHandler.toString();
		} catch (Exception e) {
			String msg = "Erro executando cmd: " + cmd2String(cmd) + ".";
			throw new RuntimeException(msg, e);
		} finally {
			quitelyClose(out);

			if (tempPath != null) {
				// se não teve interação, apaga o arquivo temporário
				try {
					Files.delete(tempPath);
				} catch (IOException e) {
					// ignore
				}
			}
		}
		
	}

	private static void throwExecutionException(ThreadedStreamHandler errorHandler, int waitFor) {
		String output = "O processo terminou com status: " + waitFor + ", saida no console: " + errorHandler.getOutput();
		throw new RuntimeException(output);
	}

	private static void quitelyClose(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	private static Process run(List<String> cmd, Path tempPath) throws IOException {
		LOGGER.info("\t\tExecuting cmd: " + cmd2String(cmd));

		final ProcessBuilder pb = new ProcessBuilder(cmd);
		if (tempPath != null) {
			// entra aqui quando não tem interação..
			// motivo: salva em um arquivo o stdout para imprimir tudo no final
			// isto foi necessário pois alguns casos o programa travava com interação
			pb.redirectErrorStream(true);
			pb.redirectOutput(tempPath.toFile());
			LOGGER.info("\t\t!!! AGUARDE !!! "
					+ "Este comando ira imprimir o resultado apenas ao final de sua execucao! "
					+ "Acompanhe o resultado do proceso no arquivo temporário: " + tempPath);
		}

		return pb.start();
	}

	private static String cmd2String(List<String> cmd) {
		final StringBuilder builder = new StringBuilder();
		for (String st : cmd) {
			builder.append(st).append(" ");
		}
		return builder.toString();
	}

}