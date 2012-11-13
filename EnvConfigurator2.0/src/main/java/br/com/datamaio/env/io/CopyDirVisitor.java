package br.com.datamaio.env.io;

import static br.com.datamaio.util.SystemUtils.isLinux;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

public class CopyDirVisitor extends SimpleFileVisitor<Path> {
	private static final Logger LOGGER = Logger.getLogger(CopyDirVisitor.class);

	private boolean keepExistingFileAttributes;
	private Map<Path, BasicFileAttributes> existingFileAttributes = new HashMap<Path, BasicFileAttributes>();
	private int level = 0;
	protected Path fromPath;
	protected Path toPath;	
	
	public CopyDirVisitor(Path from, Path to){
		this(from, to, false);
	}
	
	public CopyDirVisitor(Path from, Path to, boolean keepExistingFileAttributes){
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		
		this.fromPath = from;
		this.toPath = to;
		this.keepExistingFileAttributes = keepExistingFileAttributes;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		final Path relativize = fromPath.relativize(dir);
		final Path resolvedTargetDir = toPath.resolve(relativize);
		
		boolean goingToCreate = Files.notExists(resolvedTargetDir);
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "PRE VISIT DIR : " + dir + " (going to create target directory? " + goingToCreate + ")");
    	}
		
		if (goingToCreate) {
			Files.createDirectory(resolvedTargetDir);
		} else {
			storeCurrentFileAttributes(resolvedTargetDir);
		}
		
		level++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		final Path relativize = fromPath.relativize(file);
		final Path resolvedTargetFile = toPath.resolve(relativize);
		
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "Coping FILE "+ file + " to " + resolvedTargetFile);
		}
		
		if(Files.notExists(resolvedTargetFile)) {
			storeCurrentFileAttributes(resolvedTargetFile);
		}
		
		Files.copy(file, resolvedTargetFile, REPLACE_EXISTING);

		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		level--;

		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "POST VISIT DIR : " + dir );
		}
		
		return super.postVisitDirectory(dir, e);
	}
	
	public Map<Path, ? extends BasicFileAttributes> getExistingFileAttributes(){
		return existingFileAttributes;
	}
	
	private String tabs() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<level; i++)
			builder.append("\t");
		return builder.toString();
	}
	
	private void storeCurrentFileAttributes(final Path target) throws IOException {
		if (keepExistingFileAttributes) {
			Class<? extends BasicFileAttributes> attClass = isLinux() ? PosixFileAttributes.class : BasicFileAttributes.class;
			existingFileAttributes.put(target, Files.readAttributes(target, attClass));
		}
	}
}
