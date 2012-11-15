package br.com.datamaio.fwk.io;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public final class FileUtils {
	
	private FileUtils(){}
	
	/** Deleta um arquivo ou um diretório inteiro */
	public static void delete(Path path) {
		delete(path, "*");
	}
	
	/** Deleta um arquivo ou um diretório inteiro */
	public static void delete(Path path, String glob) {
		try{ 
			if(Files.exists(path)) {
				if(Files.isDirectory(path)) {
					Files.walkFileTree(path, new DeleteDirVisitor(glob));
				} else {
					PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
					if(matcher.matches(path.getFileName())) {
						Files.deleteIfExists(path);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void copy(Path source, Path target) {
		copy(source, target, "*");						
	}

	public static void copy(Path source, Path target, String glob) {
		try{
			if(Files.isDirectory(source)) {	
				Files.walkFileTree(source, new CopyDirVisitor(source, target, glob));
			} else if (Files.isRegularFile(source)) {
				if(Files.isDirectory(target)) {
					Path targetFile = PathUtils.get(target, source.getFileName());
					Files.copy(source, targetFile, REPLACE_EXISTING);
				} else {
					// Se o diretório onde estamos querendo colocar o arquivo não existir, lança uma NoSuchFileException
					// Motivo: eu não tenho como inferir se o target é um dir ou um file
					Files.copy(source, target, REPLACE_EXISTING);
				}
			} else {
				throw new RuntimeException("Não implementado!");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Path createFile(Path dir, String name) {
		try {
			return Files.createFile(PathUtils.get(dir, name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
}
