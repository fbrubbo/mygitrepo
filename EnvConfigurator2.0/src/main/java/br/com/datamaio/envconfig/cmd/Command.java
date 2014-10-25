package br.com.datamaio.envconfig.cmd;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import br.com.datamaio.fwk.io.FileUtils;

public abstract class Command {
	public static final String OS_LINUX = "Linux";
	public static final String OS_WINDOWS = "Windows";

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final Interaction NO_PRINTING = new Interaction() {
		@Override
		boolean shouldPrintCommand() {
			return false;
		}
		@Override
		boolean shouldPrintOutput() {
			return false;
		}		
	};
	
	public static class Interaction {
		boolean shouldPrintCommand() {
			return true;
		}
		boolean shouldPrintOutput() {
			return true;
		}		
		void execute(OutputStream out) throws Exception {
			// do nothing
		}
		boolean isExecutionSuccessful(int waitfor) {
			return 0 == waitfor;
		}
	}
	
	public static Command INSTANCE;
	public static synchronized final Command get() {
		if(INSTANCE==null) {
			if(isLinux()) {
				List<String> cmdList = Arrays.asList("uname -a".split(" "));			
				String dist = _run(cmdList, NO_PRINTING);
				if(dist.contains(UbuntuCommand.DIST_NAME)) {
					INSTANCE = new UbuntuCommand();
				} else if (dist.contains(CentOSCommand.DIST_NAME)) {
					INSTANCE = new CentOSCommand();
				} else {
					throw new RuntimeException("Distribuicao de Linux nao suportada : " + dist);
				}
			} else {
				INSTANCE = new WindowsCommand();
			}
		}
		return INSTANCE;
	}

	public static String osname() {
		return System.getProperty("os.name");
	}

	public static boolean isLinux() {
		String os = osname();
		return os != null ? os.toUpperCase().contains(OS_LINUX.toUpperCase()) : false;
	}
	
	public static boolean isWindows(){
        return !isLinux();
    }

	public abstract void service(String name, ServiceAction action);

	public abstract String distribution();
	public abstract void execute(String file);
	public abstract void addRepository(String repository);
	public abstract void install(String pack);
	public abstract void installFromLocalPath(String path);
	public abstract void uninstall(String pack);
	public abstract void unzip(String from, String toDir);		
	public abstract void dos2unix(String file);
	
	public abstract void groupadd(final String group);
	public abstract void groupadd(final String group, final String options);
	public abstract void useradd(final String user);
	public abstract void useradd(final String user, final String options);
	public abstract void passwd(final String user, final String passwd);
	
	public abstract void chmod(String mode, String file);
	public abstract void chmod(String mode, String file, boolean recursive);
	public abstract void chown(String user, String file);
	public abstract void chown(String user, String file, boolean recursive);
	public abstract void chown(String user, String group, String file, boolean recursive);

	public void ln(final String link, final String targetFile) {
		FileUtils.createSymbolicLink(Paths.get(link), Paths.get(targetFile));
	}	
	
	public boolean exists(String file){
		return Files.exists(Paths.get(file));
	}
	
	public String whoami() {
		return System.getProperty("user.name");
	}

	public void mkdir(String dir) {
		logCmdJava(format("mkdir %s", dir));  
		FileUtils.createDirectories(Paths.get(dir));
	}

	public void mv(String from, String to) {
		logCmdJava(format("mv %s to %s", from, to));
		FileUtils.move(Paths.get(from), Paths.get(to));
	}

	public List<String> ls(String path) {
		logCmdJava(format("ls %s", path));
		return FileUtils.ls(Paths.get(path)).stream().map(p -> p.toString()).collect(toList());
	}

	public void rm(String path) {
		logCmdJava(format("rm %s", path));
		FileUtils.delete(Paths.get(path));
	}

	public void cp(String from, String to) {
		logCmdJava(format("cp %s to %s", from, to));
		FileUtils.copy(Paths.get(from), Paths.get(to));
	}
	
	// --- run ---
	
	public String run(String cmd) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList);
	}
	
	public String run(List<String> cmdList) {
		return run(cmdList, true);
	}

	public String run(String cmd, final boolean prettyPrint) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, prettyPrint);
	}
	
	public String run(List<String> cmdList, final boolean shouldPrintOutput) {
		return run(cmdList, new Interaction() {
			@Override
			boolean shouldPrintOutput() {
				return shouldPrintOutput;
			}
		});
	}

	public String run(String cmd, final int... successfulExec) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, successfulExec);
	}
	
	public String run(List<String> cmdList, final int... successfulExec) {
		return run(cmdList, new Interaction() {
			@Override
			boolean isExecutionSuccessful(int waitfor) {
				return Arrays.binarySearch(successfulExec, waitfor) != -1;
			}
		});
	}
	
	public String run(String cmd, Interaction interact) {
		List<String> cmdList = Arrays.asList(cmd.split(" "));
		return run(cmdList, interact);
	}
	
	public String run(List<String> cmdList, Interaction interact) { 
		return _run(cmdList, interact);
	}
	
	// --- run with no iteraction ---
	
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
		return run(cmdList, (Interaction) null);
	}


	// ----------- Private methods -------------
	
	private static String _run(List<String> cmd, Interaction interact) {
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

			final Process process = run(cmd, tempPath, interact.shouldPrintCommand());
			if (interact != null) {
				InputStream in = process.getInputStream();
				InputStream ein = process.getErrorStream();
				out = process.getOutputStream();

				handler = new ThreadedStreamHandler(in, interact.shouldPrintOutput());
				errorHandler = new ThreadedStreamHandler(ein, interact.shouldPrintOutput());
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
				// se não teve interação, imprime todo o stdout aqui após finalizar o processo
				// isto é necessário para o usuário saber o que está acontecendo
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
			quitellyClose(out);
			deleteTempFile(tempPath);
		}
		
	}

	private static void deleteTempFile(Path tempPath) {
		if (tempPath != null) {
			// se não teve interação, apaga o arquivo temporário
			try {
				Files.delete(tempPath);
			} catch (IOException e) {
				// ignore
			}
		}
	}

	private static void throwExecutionException(ThreadedStreamHandler errorHandler, int waitFor) {
		String output = "O processo terminou com status: " + waitFor + ", saida no console: " + errorHandler.getOutput();
		throw new RuntimeException(output);
	}

	private static void quitellyClose(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	private static Process run(List<String> cmd, Path tempPath, boolean shouldPrint) throws IOException {
		if(shouldPrint) {
			LOGGER.info("\tExecuting cmd: " + cmd2String(cmd));
		}

		final ProcessBuilder pb = new ProcessBuilder(cmd);
		if (tempPath != null) {
			// entra aqui quando não tem interação..
			// motivo: salva em um arquivo o stdout para imprimir tudo no final
			// isto foi necessário pois alguns casos o programa travava com interação
			pb.redirectErrorStream(true);
			pb.redirectOutput(tempPath.toFile());
			if(shouldPrint) {
				LOGGER.info("\t\t!!! AGUARDE !!! "
					+ "Este comando ira imprimir o resultado apenas ao final de sua execucao! "
					+ "Acompanhe o resultado do proceso no arquivo temporário: " + tempPath);
			}
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
	
	private void logCmdJava(String msg) {
		LOGGER.info(String.format("\tExecuting cmd: %s (JAVA) ", msg));
	}

	
	private static class ThreadedStreamHandler extends Thread {

		private final InputStream in;
		private boolean shouldPrint = false;
		private final StringBuilder output = new StringBuilder();

		ThreadedStreamHandler(InputStream in, boolean shouldPrint) {
			this.in = in;
			this.shouldPrint = shouldPrint;
		}

		@Override
		public void run() {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while ((line = br.readLine()) != null) {
					if (shouldPrint) {
						LOGGER.info("\t\t" + line);
					}
					output.append(line + "\n");
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public String getOutput() {
			return output.toString();
		}
	}
	
}