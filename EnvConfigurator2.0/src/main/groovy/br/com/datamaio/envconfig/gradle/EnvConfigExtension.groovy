package br.com.datamaio.envconfig.gradle;

class EnvConfigExtension {
    EnvConfigExtension() {

    }
}

class EnvNestedExtension{
	String[] ipsProd = []
	String[] ipsHom  = []
	String[] ipsTst  = []

    void validate(){
    }
}

class InstallNestedExtension{
	String conf
	String module

    void validate(){
    }
}