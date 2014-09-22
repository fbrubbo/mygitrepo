package br.com.datamaio.envconfig.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfEnvironments {
	private List<String> ipProd;
	private List<String> ipHom;
	private List<String> ipTst;
	
	public ConfEnvironments() {
		this((List<String>)null, null, null);
	}
	
	public ConfEnvironments(String[] ipProd, String[] ipHom, String[] ipTst) {
		this(ipProd!=null ? Arrays.asList(ipProd) : null,
			ipHom!=null ? Arrays.asList(ipHom) : null,
			ipTst!=null ? Arrays.asList(ipTst) : null);
	}
	
	public ConfEnvironments(List<String> ipProd, List<String> ipHom, List<String> ipTst) {
		super();
		this.ipProd = ipProd!=null ? ipProd : new ArrayList<String>();
		this.ipHom = ipHom!=null ? ipHom : new ArrayList<String>();
		this.ipTst = ipTst!=null ? ipTst : new ArrayList<String>();
	}

	public boolean isProd(final String address) {
        return ipProd.contains(address);
	}
	
	public boolean isHom(final String address) {
        return ipHom.contains(address);
	}
	
	public boolean isTst(final String address) {
        return ipTst.contains(address);
	}
}