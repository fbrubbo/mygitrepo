package br.com.datamaio.envconfig.gradle;

class EnvConfigExtension {
}

class EnvNestedExtension{
	String[] ipsProd = []
	String[] ipsHom  = []
	String[] ipsTst  = []
}

class InstallNestedExtension{
	String config
	String module
}