package br.com.datamaio.util;

public class SystemUtils {

	public static final String OS_LINUX = "Linux";
	public static final String OS_WINDOWS = "Windows";

	public static String osname() {
		return System.getProperty("os.name");
	}

	public static boolean isLinux() {
		String os = osname();
		return os != null ? os.toUpperCase().contains(OS_LINUX.toUpperCase()) : false;
	}

	public static String whoami() {
		return System.getProperty("user.name");
	}
}
