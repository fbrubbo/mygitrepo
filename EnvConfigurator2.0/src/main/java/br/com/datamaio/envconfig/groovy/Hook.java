package br.com.datamaio.envconfig.groovy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import br.com.datamaio.envconfig.Constants;
import br.com.datamaio.envconfig.util.Util;

public class Hook {
	private static final Map<String, String> HOSTS = new HashMap<String, String>();
	
	protected Properties props;
	private final Properties transientProps = new Properties();

	public boolean pre() {return true;}
	public void post() {}

	/**
	 * Ao finalizar a execu��o dos hooks (pre/post) o framewok deve chamar este m�todo
	 * para limpar as propriedades transientes
	 */
	public void finish() {
		Set<Object> keySet = transientProps.keySet();
		for (Object key : keySet) {
			props.remove(key);
		}
	}

	protected boolean isLinux(){
		return Util.isLinux();
	}

	protected boolean isWindows(){
        return !Util.isLinux();
    }

	protected String whoami(){
		return Util.whoami();
	}

	protected String run(final String cmd) {
		return Util.run(cmd);
	}

	protected String chmod(final String mode, final String file) {
		return Util.chmod(mode, file);
	}

	public static String chown(final String user, final String file) {
		return Util.chown(user, file);
	}

	protected String mv(final String from, final String to) {
        return Util.mv(from, to);
    }

	protected boolean isDesenv(){
		return !isTst() && !isHom() && !isProd();
	}

	protected boolean isTst(){
		return checkEnv(Constants.ENV_TST_PROPERTY);
	}

	protected boolean isHom(){
		return checkEnv(Constants.ENV_HOM_PROPERTY);
	}

	protected boolean isProd(){
		return checkEnv(Constants.ENV_PROD_PROPERTY);
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

	private boolean checkEnv(final String property) {
		final String prop = props.getProperty(property);
		if(prop == null) {
		    throw new RuntimeException("Favor setar a propriedade " + property);
		}
		final List<String> propList = Arrays.asList(prop.split(","));

		final String address = whatIsMyIp();
        return propList.contains(address);
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
	
}
