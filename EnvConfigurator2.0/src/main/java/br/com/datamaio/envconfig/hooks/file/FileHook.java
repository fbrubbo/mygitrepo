package br.com.datamaio.envconfig.hooks.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.datamaio.envconfig.hooks.Hook;


public abstract class FileHook extends Hook {
	protected String src;
	protected String target;
	
	protected void chmod(String mode) {
		chmod(mode, target);
	}
	
	protected void chmod(String mode, boolean recursive) {
		chmod(mode, target, recursive);
	}
	
	protected void dos2unix() {
		dos2unix(target);	
	}
	
	protected void chown(String user) {
		chown(user, target);
	}
	
	protected void chown(String userAndGroup, boolean recursive) {
		chown(userAndGroup, target, recursive);
	}
	
	protected void ln(String link) {
		ln(link, target);
	}
		
	protected void renameTo(String to) {
        mv(target, to);
    }
	
	protected void reloadFile() {
		execute(target);
	}
	
	protected String getTargetDirectory(){
		Path path = Paths.get(target);
		if(Files.isDirectory(path)){
			return target;
		}
		return path.getParent().toString();
	}

	protected void setSrc(String srcPath) {
		this.src = srcPath;
	}

	protected void setTarget(String targetPath) {
		this.target = targetPath;
	}	
}