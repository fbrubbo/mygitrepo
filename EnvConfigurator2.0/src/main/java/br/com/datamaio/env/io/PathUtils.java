package br.com.datamaio.env.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
	public static Path get(Path dir, Path name) {
		return get(dir, name.toString());
	}
	
	public static Path get(Path dir, String name) {
		return Paths.get(dir.toString(), name);
	}
	
	public static Path resolve(Path file, Path srcDir, Path destDir) {
		return destDir.resolve(srcDir.relativize(file));
	}
}
