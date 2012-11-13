package br.com.datamaio.env.io;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public final class FileUtils {
	
	private FileUtils(){}
	
	/** Deleta um arquivo ou um diret�rio inteiro */
	public static void delete(Path path) {
		delete(path, "*");
	}
	
	/** Deleta um arquivo ou um diret�rio inteiro */
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
		copy(source, target, false);				
	}

	public static void copy(Path source, Path target, boolean keepExistingPosixAttributes) {
		try{
			if(Files.isDirectory(source)) {	
				CopyDirVisitor visitor = new CopyDirVisitor(source, target, keepExistingPosixAttributes);
				Files.walkFileTree(source, visitor);
				if(keepExistingPosixAttributes) {
					Map<Path, ? extends BasicFileAttributes> existingFileAttributes = visitor.getExistingFileAttributes();
					// TODO: iterar os existingFileAttributes
					// CUIDADO: uma vez que eu j� substitu� os file atributes com os novos.. ser� que estes objetos n�o v�o ter sido afetado? se for uma refer�ncia, sim..
				}
			} else if (Files.isRegularFile(source)) {
				if(Files.isDirectory(target)) {
					Path targetFile = PathUtils.get(target, source.getFileName());
					Files.copy(source, targetFile, REPLACE_EXISTING);
				} else {
					// Se o diret�rio onde estamos querendo colocar o arquivo n�o existir, lan�a uma NoSuchFileException
					// Motivo: eu n�o tenho como inferir se o target � um dir ou um file
					Files.copy(source, target, REPLACE_EXISTING);
				}
			} else {
				throw new RuntimeException("N�o implementado!");
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
