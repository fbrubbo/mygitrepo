package br.com.datamaio.fwk.io;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

import org.apache.log4j.Logger;

public final class FileUtils {
	
	private static final Logger LOGGER = Logger.getLogger(FileUtils.class);
	
	private FileUtils(){}
	
	/** Deleta um arquivo ou um diret�rio inteiro */
	public static void delete(Path path) {
		delete(path, "*");
	}
	
	/** 
	 * Deleta um arquivo ou um diret�rio
	 * 
	 * @param path caminho que se deseja apagar
	 * @param glob padr�o glob para arquivos. Ex: <code>*.txt</code> ir� apagar apenas arquivos .txt 
	 */
	public static void delete(Path path, String glob) {
		try{ 
			if(Files.exists(path)) {
				if(Files.isDirectory(path)) {
					Files.walkFileTree(path, new DeleteVisitor(glob));
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
	
	/** 
	 * Copia todos arquivos da fonte para o destino. <br>
	 * Isto ir� sobrescrever todo e qualquer arquivo do destino
	 * 
	 * @param source fonte. Pode ser aquivo ou diret�rio
	 * @param target destino. Pode ser aquivo ou diret�rio
	 */
	public static void copy(Path source, Path target) {
		copy(source, target, "*");						
	}

	/** 
	 * Copia ALGUNS arquivos da fonte para o destino.<br>
	 * Isto ir� sobrescrever todo e qualquer arquivo do destino
	 * 
	 * @param source fonte. Pode ser aquivo ou diret�rio
	 * @param target destino. Pode ser aquivo ou diret�rio
	 * @param glob padr�o glob para arquivos. Ex: <code>*.txt</code> ir� copiar apenas arquivos .txt 
	 */
	public static void copy(Path source, Path target, String glob) {
		try{
			if(Files.isDirectory(source)) {	
				Files.walkFileTree(source, new CopyVisitor(source, target, glob));
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

	/** M�todo helper para criar um arquivo em um determinado diret�rio com um determinado nome */
	public static Path createFile(Path dir, String name) {
		try {
			return Files.createFile(PathUtils.get(dir, name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}

	public static void createDirectories(Path dir) {
		try {
			if (Files.notExists(dir)) {
				LOGGER.trace(dir + " does not exist. Creating...");
				Files.createDirectories(dir);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
}
