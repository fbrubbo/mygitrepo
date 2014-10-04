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

class SettingsNestedExtention{
}

class LinuxNestedExtention{
	boolean usesudo=false
}

class WindowsNestedExtention{
}