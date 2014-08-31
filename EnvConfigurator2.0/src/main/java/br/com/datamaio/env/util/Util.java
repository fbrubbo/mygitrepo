package br.com.datamaio.env.util;

import static br.com.datamaio.env.Constants.LINUX_USE_SUDO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// FIXME: reescrever toda esta clase para Cmd
// Ai criar para LinuxCmd, CentOSCmd, WindowsCmd, etc
public class Util {

	static class Interact {
		boolean printOutput(){
			return false;
		}
		void execute(final OutputStream out) throws Exception {
			// do nothing
		}
	}

	public static final String OS_LINUX = "Linux";
	public static final String OS_WINDOWS = "Windows";

	public static String osname(){
		return System.getProperty("os.name");
	}

	public static boolean isLinux(){
		final String os = osname();
		return os!=null ? os.toUpperCase().contains(OS_LINUX.toUpperCase()) : false;
	}

	public static String whoami(){
	    return System.getProperty("user.name");
	}

	public static String chmod(final String mode, final String file) {
		return chmod(mode, file, false);
	}

	public static String chmod(final String mode, final String file, final boolean recursive) {
		final List<String> cmd = new ArrayList<String>();
		cmd.add("chmod");
		if(recursive) {
			cmd.add("-R");
		}
		cmd.add(mode);
		cmd.add(file);
		final String ret = runWhenOnLinux(cmd);

		if(file.endsWith(".sh")) {
            // executa este cara apenas para garantir que se alguem salvou no windows
            // este arquivo possa ser executado no windows
            dos2unix(file);
        }

		return ret;
	}

	public static void dos2unix(final String file) {
		final List<String> cmd = new ArrayList<String>();
		cmd.add("dos2unix");
		cmd.add(file);
		runWhenOnLinux(cmd);
	}

	public static String chown(final String user, final String file) {
		return chown(user, file, false);
	}

	public static String chown(final String user, final String file, final boolean recursive) {
		return chown(user, user, file, recursive);
	}

	public static String chown(final String user, final String group, final String file, final boolean recursive) {
		final List<String> cmd = new ArrayList<String>();
		cmd.add("chown");
		if(recursive) {
			cmd.add("-R");
		}
		cmd.add(user + ":" + group);
		cmd.add(file);
		return runWhenOnLinux(cmd);
	}

	public static String mv(final String from, final String to) {
        final List<String> cmd = new ArrayList<String>();
        cmd.add("mv");
        cmd.add(from);
        cmd.add(to);
        return runWhenOnLinux(cmd);
    }

	public static String ls(final String path){
        return run("ls " + path);
	}

	public static String ln(final String file, final String link){
        return run("ln -sf " + file + " " + link);
	}

	public static String rm(final String path) {
		return run("rm -f " + path);
	}

	public static String groupadd(final String group) {
		return groupadd(group, null);
	}

	public static String groupadd(final String group, final String opt) {
		return run("groupadd "+ (opt!=null ? opt : "-f") + " " + group );
	}

	public static String useradd(final String user) {
		return useradd(user, null);
	}

	public static String useradd(final String user, final String opt) {
		throw new RuntimeException("TODO: não conseguimos fazer funcionar ainda porque o java quer que rode um comando e estamos tentando rodar um if");
		//return run("if (! getent passwd hacluster >/dev/null); then useradd " + user + (opt!=null ? " " + opt : "") + " 2>/dev/null ; fi");
	}

	public static String passwd(final String passwd) {
		return run("passwd " + passwd);
	}

	public static String run(final String cmd) {
		final List<String> cmdList = Arrays.asList(cmd.split(" "));
		return runWhenOnLinux(cmdList);
	}

	/**
	 * INFO: requer que o SELinux esteja desabilitado, caso contrario a execucao falha
	 *
	 * @param pack o nome do pacote que se deseja instalar
	 */
	public static void install(final String pack){
	    System.out.println("\t********** Instalando pacote " + pack );
		final List<String> cmd = Arrays.asList(new String[]{"yum", "install", pack});
		runWhenOnLinux(cmd, new Interact(){
			@Override
			boolean printOutput(){
				return true;
			}
			@Override
			void execute(final OutputStream out) throws Exception {
				// imprime o y para dizer para instalar
				out.write("y\n".getBytes());
			}
		});
	    System.out.println("\t********** Pacote " + pack + " instalado\n");
	}

	/**
	 * INFO: requer que o SELinux esteja desabilitado, caso contrario a execucao falha
	 *
	 * @param pack o nome do pacote que se deseja remover
	 */
	public static void remove(final String pack){
	    System.out.println("\t********** Removendo pacote " + pack );
		final List<String> cmd = Arrays.asList(new String[]{"yum", "erase", pack});
		runWhenOnLinux(cmd, new Interact(){
			@Override
			boolean printOutput(){
				return true;
			}
			@Override
			void execute(final OutputStream out) throws Exception {
				// imprime o y para dizer para desinstalar
				out.write("y\n".getBytes());
			}
		});
		System.out.println("\t********** Pacote " + pack + " removido\n");
	}

	public static String runWhenOnLinux(final List<String> cmd) {
		return runWhenOnLinux(cmd, new Interact());
	}

	public static String runWhenOnLinux(final List<String> cmd, final Interact interact) {
		if(isLinux()) {
	    	final boolean useSudo = Boolean.valueOf(System.getProperty(LINUX_USE_SUDO));
		    try {
		    	final Process process = run(cmd, useSudo);

		    	final InputStream in = process.getInputStream();
		    	final OutputStream out = process.getOutputStream();
		    	final ThreadedStreamHandler handler = new ThreadedStreamHandler(in, out);
		    	handler.setPrintoutput(interact.printOutput());

		    	final InputStream ein = process.getErrorStream();
		    	final ThreadedStreamHandler eHandler = new ThreadedStreamHandler(ein);
		    	eHandler.setPrintoutput(interact.printOutput());

		    	handler.start();
		    	eHandler.start();

		    	// se o usuario definiu algum tipo de interacao
	    		interact.execute(out);
	    		out.flush();

		    	final int waitFor = process.waitFor();

		        handler.interrupt();
		        eHandler.interrupt();
		        handler.join();
		        eHandler.join();

		        if(waitFor!=0) {
                    System.out.println("Exist code: " + waitFor);
                    throw new RuntimeException(eHandler.getOutput());
                }

		        return handler.getOutput();
			} catch (final Exception e) {
				String msg = "Erro executando cmd: " + buildCmd(cmd, useSudo) + ".";
				if(!useSudo) {
						msg += " Caso precise de permissao de super usuario para alguma atividade desta instalacao, tente " +
								"chamar o metodo requestSudoPassword() antes de iniciarlizar a instalacao do modulo no Hook.groovy.";
				}
				throw new RuntimeException(msg, e);
			}
		} else {
			//Comando nao executado! Motivo: rodando no windows
		}

		return null;
	}

	private static Process run(final List<String> cmd, final boolean useSudo)
			throws IOException {

	    System.out.println("\t\tExecuting cmd: " + buildCmd(cmd, useSudo) );

		final List<String> list = new ArrayList<String>();
		if(useSudo){
			list.add("sudo");
		}
		list.addAll(cmd);
		final ProcessBuilder pb = new ProcessBuilder(list);

		return pb.start();
	}

	private static String buildCmd(final List<String> cmd, final boolean useSudo) {
		final StringBuilder builder = new StringBuilder();
		builder.append(useSudo ? "sudo " : "");
		for (final String st : cmd) {
			builder.append(st).append(" ");
		}
		return builder.toString();
	}
}
