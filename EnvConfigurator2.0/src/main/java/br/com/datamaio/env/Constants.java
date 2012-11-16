package br.com.datamaio.env;

public interface Constants {
	public static final String ENV_PROD_PROPERTY = "EnvConfigurator.list.ip.prod";
	public static final String ENV_HOM_PROPERTY = "EnvConfigurator.list.ip.hom";
	public static final String ENV_TST_PROPERTY = "EnvConfigurator.list.ip.tst";

	public static final String ENCRYPTOR_PASSWORD_PROPERTY = "EnvConfigurator.encryptor.password";
    public static final String LINUX_USE_SUDO = "EnvConfigurator.linux.usesudo";
    public static final String DO_NOT_BACKUP = "EnvConfigurator.do.not.backup.existing.conf";

    public static final String ENABLE_DOS2UNIX_4_TEXTFILES = "EnvConfigurator.enable.dos2unix4TextFiles";
}
