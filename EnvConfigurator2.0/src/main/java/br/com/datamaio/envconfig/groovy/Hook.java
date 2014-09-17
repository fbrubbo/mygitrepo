package br.com.datamaio.envconfig.groovy;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.conf.Environments;
import br.com.datamaio.envconfig.util.cmd.Command;

public class Hook {
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final Map<String, String> HOSTS = new HashMap<String, String>();
	
	protected Environments envs;
	protected Properties props;
	protected Configuration conf;
	private final Properties transientProps = new Properties();
	protected final Command command;

	public boolean pre() {return true;}
	public void post() {}

	public Hook(){
		command = Command.get();
	}
	
	/**
	 * Ao finalizar a execução dos hooks (pre/post) o framewok deve chamar este método
	 * para limpar as propriedades transientes
	 */
	public void finish() {
		Set<Object> keySet = transientProps.keySet();
		for (Object key : keySet) {
			props.remove(key);
		}
	}

	protected boolean isLinux(){
		return Command.isLinux();
	}

	protected boolean isWindows(){
        return !Command.isLinux();
    }

	protected String whoami(){
		return command.whoami();
	}

	protected String run(final String cmd) {
		return command.run(cmd);
	}

	protected String chmod(final String mode, final String file) {
		return command.chmod(mode, file);
	}

	public String chown(final String user, final String file) {
		return command.chown(user, file);
	}

	protected String mv(final String from, final String to) {
        return command.mv(from, to);
    }

	protected boolean isDesenv(){
		return !isTst() && !isHom() && !isProd();
	}

	protected boolean isTst(){
		final String address = whatIsMyIp();
		return envs.isTst(address);
	}

	protected boolean isHom(){
		final String address = whatIsMyIp();
		return envs.isHom(address);
	}

	protected boolean isProd(){
		final String address = whatIsMyIp();
		return envs.isProd(address);
	}

    protected String whatIsMyIp()
    {
        try {
			final InetAddress addr = InetAddress.getLocalHost();
	        String ip = addr.getHostAddress();
	        if("127.0.0.1".equals(ip)){ 
	        	// Cai aqui quando tem no /etc/hosts a identificação do nome com 127.0.0.1
	        	ip = getIpFromDNS(addr.getHostName());
	        }
			return ip;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
    }
    
    protected String whatIsMyHostName()
    {
        try {
			final InetAddress addr = InetAddress.getLocalHost();
	        return addr.getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
    }
    
	protected void addTransientProperty(final String key, final Boolean value){
		addTransientProperty(key, "" + value);
	}

	protected void addTransientProperty(final String key, final String value){
		props.put(key, value);
		transientProps.put(key, value);
	}

	protected void addPersistentProperty(final String key, final String value){
		props.put(key, value);
	}

	protected String get(final String key){
	    return props.getProperty(key);
	}

	protected boolean contains(final String key){
        return props.getProperty(key)!=null;
    }


	private String getIpFromDNS(String hostName) {
		if(!HOSTS.containsKey(hostName)) { 
			System.out.println("\t\t\tBuscando IP no DNS para o host " + hostName);
			final List<String> dnsRecs = getDNSRecs(hostName, "A");
			final String ip = dnsRecs.size()>0 ? dnsRecs.get(0) : "127.0.0.1";
			HOSTS.put(hostName, ip);
		}
		return HOSTS.get(hostName);
	}
	
	 /**
     * Rertorna todos os registros do DNS para um dado dominio
     *
     * @param domain domínio, e.g. xyz.dbserver.com.br, no qual você deseja conhecer os registros do DNS.
     * @param types  e.g."MX","A" para descrever quais registros vc deseja.
     * 			<ul>
     * 				<li> MX: o resultado contém a prioridade (lower better) seguido pelo mailserver
     * 				<li> A: o resultado contém apenas o IP
     * 			</ul>
     *
     * @return lista de resultados
     */
	private List<String> getDNSRecs(String domain, String... types) {
		
		List<String> results = new ArrayList<String>(15);

		try {
			final Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
			
			final DirContext ictx = new InitialDirContext(env);
			final Attributes attrs = ictx.getAttributes(domain, types);
			for (NamingEnumeration<? extends Attribute> e = attrs.getAll(); e.hasMoreElements();) {
				final Attribute a = (Attribute) e.nextElement();
				for (int i = 0; i < a.size(); i++) {
					results.add((String) a.get(i));
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		if (results.size() == 0) {
			System.err.println("Falha para encontrar um registro no DNS para o domínio " + domain);
		}
		return results;
	}
	
    public void log(String msg) {
        LOGGER.info(msg);
    }
    
    public Path resolveDependency(String name) {
    	File file = conf.getDependency(name);
    	if(file==null)
    		throw new RuntimeException("Não foi possível resolver a dependência " + name);
    	
    	return file.toPath();
    }
	
}
