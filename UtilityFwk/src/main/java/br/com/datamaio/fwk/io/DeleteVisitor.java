package br.com.datamaio.fwk.io;

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


public class DeleteVisitor extends SimpleFileVisitor<Path> {
	private static final int NONE = Integer.MAX_VALUE;
	private static final Logger LOGGER = Logger.getLogger(DeleteVisitor.class);
	
	private int level = 0;			
    private int goingToDeleteDirAtLevel = NONE;
    private final PathMatcher matcher;

	public DeleteVisitor(){
    	this("*");
    }
    
    public DeleteVisitor(String glob){
    	Objects.requireNonNull(glob);
    	this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
    	LOGGER.trace("VISITOR INITIALIZED (Matcher: " + glob + ")");
    }
    
    public DeleteVisitor(PathMatcher matcher) {
    	Objects.requireNonNull(matcher);    	
    	this.matcher = matcher;
    	LOGGER.trace("VISITOR INITIALIZED (Matcher: " + matcher + ")");
    }
    
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {		    	
    	if(goingToDeleteDirAtLevel==NONE && matcher.matches(dir.getFileName())) {
			goingToDeleteDirAtLevel = level;
		}
    	
    	if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "PRE VISIT DIR : " + dir + "(going to delete? " + (level>=goingToDeleteDirAtLevel) + ")");
    	}
    	
		level++;		
		return super.preVisitDirectory(dir, attrs);
    }
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		boolean goingToDelete = mustDelete(file);
		
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "VISIT FILE "+ file  + "(going to delete? " + goingToDelete + ")");
		}
		
		if(goingToDelete) {
			delete(file);	
		}
		return FileVisitResult.CONTINUE;
	}

	protected void delete(Path file) throws IOException {
		Files.delete(file);
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		level--;
		boolean goingToDelete = mustDelete(dir);

		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace(tabs() + "POST VISIT DIR : " + dir + "(going to delete? " + goingToDelete + ")");
		}

		if(goingToDeleteDirAtLevel==level) {
			goingToDeleteDirAtLevel = NONE;
		}
    	
		if (e == null) {
			if(goingToDelete) {
				delete(dir);	
			}			
			return FileVisitResult.CONTINUE;
		}
		throw e;
	}

	protected boolean mustDelete(Path path) {
		return level>goingToDeleteDirAtLevel || matcher.matches(path.getFileName());
	}

	private String tabs() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<level; i++)
			builder.append("\t");
		return builder.toString();
	}
}