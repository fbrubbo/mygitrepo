package br.com.datamaio.fwk.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathUtils {
	
	private PathUtils(){}
	
	/** Delegate para @link {@link Paths#get(String, String...)} */
	public static Path get(String first, String... more) {
		return Paths.get(first, more);
	}
	
	/** Método helper para pegar o caminho de um arquivo */
	public static Path get(Path dir, Path name) {
		return get(dir, name.toString());
	}
	
	/** Método helper para pegar o caminho de um arquivo */
	public static Path get(Path dir, String name) {
		return Paths.get(dir.toString(), name);
	}
	
	/** Método helper para resolver o caminho de um arquivo em um destino */
	public static Path resolve(Path file, Path srcDir, Path destDir) {
		return destDir.resolve(srcDir.relativize(file));
	}
}
