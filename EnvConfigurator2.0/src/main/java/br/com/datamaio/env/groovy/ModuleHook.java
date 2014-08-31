package br.com.datamaio.env.groovy;

import br.com.datamaio.env.Constants;
import br.com.datamaio.env.util.Util;


public class ModuleHook extends Hook {

	private static final String ROOT = "root";

    protected void requestSudoPassword() {
    	if(ROOT.equals(whoami())){
    		System.out.println(">> Eu sou ROOT, sendo assim nao preciso de SUDO!");
    	} else {
	        System.setProperty(Constants.LINUX_USE_SUDO, "true");
	        System.out.println("Entre com o password para executar os comandos com sudo. (OBS: o comando abaixo eh apenas para verificar a permissao de sudo)");
	        final String result = Util.ls("/root/");

	        // o if abaixo � apenas por garantia. Vai que não d� erro e apenas aparece permiss�o negada no output
	        if(result==null
	                || result.contains("Permission denied")
	                || result.contains("Permissão negada")
	                || result.contains("Permiss�o negada")
	                || result.contains("não permitida")
	                || result.contains("não permitida")) {
	            throw new RuntimeException("Permiss�o negada!");
	        }
    	}
    }

}