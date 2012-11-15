package br.com.datamaio.fwk.io;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import org.apache.log4j.Logger;

public class CopyDirVisitor extends SimpleFileVisitor<Path> {
	private static final Logger LOGGER = Logger.getLogger(CopyDirVisitor.class);

	private int level = 0;
	protected Path fromPath;
	protected Path toPath;
	private final PathMatcher matcher;
	
	public CopyDirVisitor(Path from, Path to){
		this(from, to, "*");
	}
	
    public CopyDirVisitor(Path from, Path to, String glob){
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
    	Objects.requireNonNull(glob);
		
		this.fromPath = from;
		this.toPath = to;
    	this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
    	LOGGER.trace("VISITOR INITIALIZED (Matcher: " + glob + ")");
    }
    
    public CopyDirVisitor(Path from, Path to, PathMatcher matcher) {
    	Objects.requireNonNull(from);
		Objects.requireNonNull(to);
    	Objects.requireNonNull(matcher);
		
		this.fromPath = from;
		this.toPath = to;
    	this.matcher = matcher;
    	LOGGER.trace("VISITOR INITIALIZED (Matcher: " + matcher + ")");
    }
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		final Path relativize = fromPath.relativize(dir);
		final Path resolvedTargetDir = toPath.resolve(relativize);
		
		boolean goingToCreate = Files.notExists(resolvedTargetDir) && matcher.matches(dir.getFileName());
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "PRE VISIT DIR : " + dir + " (going to create target directory? " + goingToCreate + ")");
    	}
		
		if (goingToCreate) {
			Files.createDirectories(resolvedTargetDir);
		} 
		
		level++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		final Path relativize = fromPath.relativize(file);
		final Path resolvedTargetFile = toPath.resolve(relativize);
		
		if(matcher.matches(file.getFileName())) {
			LOGGER.trace(tabs() + "Coping FILE "+ file + " to " + resolvedTargetFile);
			Files.copy(file, resolvedTargetFile, REPLACE_EXISTING);
		} else {
			LOGGER.trace(tabs() + "Ignoring FILE "+ file + " to " + resolvedTargetFile);
		}

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
	
	private String tabs() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<level; i++)
			builder.append("\t");
		return builder.toString();
	}
	
}
