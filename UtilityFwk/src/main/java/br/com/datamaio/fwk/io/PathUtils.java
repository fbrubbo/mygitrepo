package br.com.datamaio.fwk.io;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class PathUtils {
	
	private PathUtils(){}
	
	/** Delegate para @link {@link Paths#get(String, String...)} */
	public static Path get(String first, String... more) {
		return Paths.get(first, more);
	}
	
	/** Método helper para pegar o caminho de um arquivo */
	public static Path get(Path dir, Path... more) {
		String[] moreStr = Arrays.stream(more)
							.map(p -> p.toString())
							.collect(toList())
							.toArray(new String[]{});
		return get(dir, moreStr);
	}
	
	/** Método helper para pegar o caminho de um arquivo */
	public static Path get(Path dir, String... more) {
		return Paths.get(dir.toString(), more);
	}
	
	/** Método helper para resolver o caminho de um arquivo em um destino */
	public static Path resolve(Path file, Path srcDir, Path destDir) {
		return destDir.resolve(srcDir.relativize(file));
	}
}
