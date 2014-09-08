package br.com.datamaio.envconfig.util;

import static br.com.datamaio.envconfig.Constants.LINUX_USE_SUDO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
// FIXME: reescrever toda esta clase para Cmd
// Ai criar para LinuxCmd, UbuntoCmd, CentOSCmd, WindowsCmd, etc
// remover o use sudo?!?! se remover aqui, remover também de ModuleHook

public class Cmd
{
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    static class Interact
    {
        boolean printOutput()
        {
            return true;
        }

        void execute(OutputStream out) throws Exception
        {
            // do nothing
        }

        boolean isSuccessfulExec(int waitfor) {
            return 0 == waitfor;
        }
    }

    public static final String OS_LINUX = "Linux";
    public static final String OS_WINDOWS = "Windows";

    public static String osname()
    {
        return System.getProperty("os.name");
    }

    public static boolean isLinux()
    {
        String os = osname();
        return os != null ? os.toUpperCase().contains(OS_LINUX.toUpperCase()) : false;
    }

    public static String whoami()
    {
        return System.getProperty("user.name");
    }

    public static String chmod(String mode, String file)
    {
        return chmod(mode, file, false);
    }

    public static String chmod(String mode, String file, boolean recursive)
    {
        List<String> cmd = new ArrayList<String>();
        cmd.add("chmod");
        if(recursive)
        {
            cmd.add("-R");
        }
        cmd.add(mode);
        cmd.add(file);
        String ret = runWhenOnLinux(cmd);

        if(file.endsWith(".sh"))
        {
            // executa este cara apenas para garantir que se alguem salvou no
            // windows
            // este arquivo possa ser executado no windows
            dos2unix(file);
        }

        return ret;
    }

    public static void dos2unix(String file)
    {
        List<String> cmd = new ArrayList<String>();
        cmd.add("dos2unix");
        cmd.add(file);
        runWhenOnLinux(cmd);
    }

    public static String chown(String user, String file)
    {
        return chown(user, file, false);
    }

    public static String chown(String user, String file, boolean recursive)
    {
        return chown(user, user, file, recursive);
    }

    public static String chown(String user, String group, String file, boolean recursive)
    {
        List<String> cmd = new ArrayList<String>();
        cmd.add("chown");
        if(recursive)
        {
            cmd.add("-R");
        }
        cmd.add(user + ":" + group);
        cmd.add(file);
        return runWhenOnLinux(cmd);
    }

    public static String mkdir(String dir)
    {
        return run("mkdir -p " + dir);
    }

    public static String mv(String from, String to)
    {
        List<String> cmd = new ArrayList<String>();
        cmd.add("/bin/mv");
        cmd.add("-f");
        cmd.add(from);
        cmd.add(to);
        return runWhenOnLinux(cmd);
    }

    public static String ls(String path)
    {
        return run("ls " + path);
    }

    public static String ln(final String file, final String link){
        return run("ln -sf " + file + " " + link);
    }

    public static String rm(final String path) {
        return run("/bin/rm -f " + path);
    }

    public static String rm(final String path, final boolean isRecursive) {
        return run("/bin/rm -f" + (isRecursive ? "R" : "") + " " + path);
    }

    public static String cp(final String from, final String to) {
        return run("/bin/cp -f " + from + " " + to);
    }

    public static String groupadd(final String group) {
        return groupadd(group, null);
    }

    public static String groupadd(final String group, final String options) {
        return run("groupadd "+ (options!=null ? options : "-f") + " " + group );
    }

    public static String useradd(final String user) {
        return useradd(user, null);
    }

    public static String useradd(final String user, final String options) {
        try{
            String ret = run("id " + user, false);
            LOGGER.info("Usuario ja existe. Nao sera criado novamente.");
            return ret;
        } catch(Exception e) {
            LOGGER.info("Usuario nao existe. Criando ...");
            return run("useradd " + (options!=null ? options : "") + " "+ user);
        }
    }

    /**
     * OBS IMPORTANTE: Se o selinux estiver ligado este método não funciona.
     */
    public static String passwd(final String user, final String passwd) {
        List<String> cmd = Arrays.asList("passwd", user);
        return runWhenOnLinux(cmd, new Interact(){
            @Override
            void execute(OutputStream out) throws Exception
            {
                byte[] bytes = (passwd+"\n").getBytes();
                out.write(bytes); // entra a senha
                out.write(bytes); // confirma a senha
            }
        });
    }

    public static String run(String cmd)
    {
        return run(cmd, true);
    }

    public static String run(String cmd, final boolean printOutput)
    {
        List<String> cmdList = Arrays.asList(cmd.split(" "));
        return runWhenOnLinux(cmdList, printOutput);
    }

    public static String run(String cmd, final int... successfulExec) {
        List<String> cmdList = Arrays.asList(cmd.split(" "));
        return runWhenOnLinux(cmdList, successfulExec);
    }

    /**
     * Este metodo nao faz interacao nenhuma. Isto é, ele não mostra o output e nem o erro.
     * Mas se o retorno do comando for diferente de 0, ele continua lancando uma exception
     *
     * OBS> este metodo foi criado pois alguns executaveis travavam lendo o output
     */
    public static String runWithNoInteraction(String cmd)
    {
        List<String> cmdList = Arrays.asList(cmd.split(" "));
        return runWhenOnLinux(cmdList, (Interact) null);
    }

    // ----------- begin bash run --------

    /**
     * Commando bash foi criado pois alguns comandos não são compreendidos corretamente pelo linux quando executados pelo java.
     *
     * Por exemplo o comando:
     *      echo '777777' > ~/test
     * Quando executado diretamente no linux ele coloca o texto '777777' dentro do arquivo test
     * Quando executado pelo java ele imprime no output
     *      '777777' > ~/test
     *
     * Forma de usar:
     *       bash("echo '777777' > ~/test")
     */
    public static String bash(String cmd)
    {
        return bash(cmd, true);
    }

    public static String bash(String cmd, final boolean printOutput)
    {
        List<String> cmdList = Arrays.asList("bash", "-c", cmd);
        return runWhenOnLinux(cmdList, printOutput);
    }

    public static String bash(String cmd, final int... successfulExec) {
        List<String> cmdList = Arrays.asList("bash", "-c", cmd);
        return runWhenOnLinux(cmdList, successfulExec);
    }

    public static String bashWithNoInteraction(String cmd)
    {
        List<String> cmdList = Arrays.asList("bash", "-c", cmd);
        return runWhenOnLinux(cmdList, (Interact) null);
    }

    // ---------- end bash run -------

    /**
     * INFO: requer que o SELinux esteja desabilitado, caso contrario a execucao falha
     *
     * @param pack o nome do pacote que se deseja instalar
     */
    public static void install(String pack)
    {
        LOGGER.info("\t********** Instalando pacote " + pack);
        List<String> cmd = Arrays.asList(new String[]{"yum", "-y", "install", pack});
        runWhenOnLinux(cmd);
        LOGGER.info("\t********** Pacote " + pack + " instalado\n");
    }

    /**
     * INFO: requer que o SELinux esteja desabilitado, caso contrario a execucao falha
     *
     * @param pack o nome do pacote que se deseja remover
     */
    public static void uninstall(String pack)
    {
        LOGGER.info("\t********** Removendo pacote " + pack);
        List<String> cmd = Arrays.asList(new String[]{"yum", "erase", pack});
        runWhenOnLinux(cmd, new Interact());
        LOGGER.info("\t********** Pacote " + pack + " removido\n");
    }

    public static String runWhenOnLinux(List<String> cmd)
    {
        return runWhenOnLinux(cmd, new Interact());
    }

    public static String runWhenOnLinux(List<String> cmd, final boolean printOutput)
    {
        return runWhenOnLinux(cmd, new Interact()
        {
            @Override
            boolean printOutput()
            {
                return printOutput;
            }
        });
    }

    public static String runWhenOnLinux(List<String> cmd, final int... successfulExec) {
        return runWhenOnLinux(cmd, new Interact()
        {
            @Override
            boolean isSuccessfulExec(int waitfor) {
                return Arrays.binarySearch(successfulExec, waitfor)!=-1;
            }
        });
    }


    public static String runWhenOnLinux(List<String> cmd, Interact interact)
    {
        if(isLinux())
        {
            final boolean useSudo = Boolean.valueOf(System.getProperty(LINUX_USE_SUDO));
            ThreadedStreamHandler handler = null;
            ThreadedStreamHandler errorHandler = null;
            OutputStream out = null;
            Path tempPath = null;
            StringBuilder noInteractionHandler = new StringBuilder();
            try {
                if(interact == null) {
                    // se não tem iteração joga o conteúdo no arquivo para depois ler
                    tempPath = Files.createTempFile("cmd", ".out");
                }

                final Process process = run(cmd, useSudo, tempPath);
                if(interact != null) {
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

                if(interact != null) {
                    handler.interrupt();
                    errorHandler.interrupt();
                    handler.join();
                    errorHandler.join();
                } else {
                    // se não teve interação, imprime todo o stdout aqui após finalizar o processo
                    // isto é necessário para o usuário saber o que está acontecendo
                    for(String line : Files.readAllLines(tempPath, Charset.defaultCharset())) {
                        LOGGER.info("\t\t" + line);
                        noInteractionHandler.append(line).append("\n");
                    }
                }

                if(interact!=null) {
                    if(!interact.isSuccessfulExec(waitFor)){
                        throwExecutionException(errorHandler, waitFor);
                    }
                } else if(waitFor != 0) {
                    throwExecutionException(errorHandler, waitFor);
                }
                return (interact != null) ? handler.getOutput() : noInteractionHandler.toString() ;
            }
            catch(Exception e)
            {
                String msg = "Erro executando cmd: " + buildCmd(cmd, useSudo) + ".";
                throw new RuntimeException(msg, e);
            }
            finally {
                quitelyClose(out);

                if(tempPath!=null) {
                    // se não teve interação, apaga o arquivo temporário
                    try {
                        Files.delete(tempPath);
                    } catch(IOException e) {
                        // ignore
                    }
                }
            }
        }
        else
        {
            // Comando nao executado! Motivo: rodando no windows
        }

        return null;
    }

    private static void throwExecutionException(ThreadedStreamHandler errorHandler, int waitFor)
    {
        String output = "O processo terminou com status: " + waitFor + ", saida no console: " + errorHandler.getOutput();
        throw new RuntimeException(output);
    }


    private static void quitelyClose(OutputStream out)
    {
        if(out!=null){
            try
            {
                out.close();
            }
            catch(IOException e)
            {
            }
        }
    }

    private static Process run(List<String> cmd, boolean useSudo, Path tempPath) throws IOException
    {
        LOGGER.info("\t\tExecuting cmd: " + buildCmd(cmd, useSudo));

        final List<String> list = new ArrayList<String>();
        if(useSudo)
        {
            list.add("sudo");
        }
        list.addAll(cmd);
        final ProcessBuilder pb = new ProcessBuilder(list);

        if(tempPath != null) {
            // entra aqui quando não tem interação..
            // motivo: salva em um arquivo o stdout para imprimir tudo no final
            //      isto foi necessário pois alguns casos o programa travava com interação
            pb.redirectErrorStream(true);
            pb.redirectOutput(tempPath.toFile());
            LOGGER.info("\t\t!!! AGUARDE !!! Este comando ira imprimir o resultado apenas ao final de sua execucao! Acompanhe o resultado do proceso no arquivo temporário: " + tempPath);
        }

        return pb.start();
    }

    private static String buildCmd(List<String> cmd, boolean useSudo)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(useSudo ? "sudo " : "");
        for(String st : cmd)
        {
            builder.append(st).append(" ");
        }
        return builder.toString();
    }
}