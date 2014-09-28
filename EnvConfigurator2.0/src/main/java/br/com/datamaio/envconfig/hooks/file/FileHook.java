package br.com.datamaio.envconfig.hooks.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.datamaio.envconfig.hooks.Hook;


public abstract class FileHook extends Hook {
	protected String src;
	protected String target;
	
	protected String chmod(String mode) {
		return chmod(mode, target);
	}
	
	protected String chmod(String mode, boolean recursive) {
		return chmod(mode, target, recursive);
	}
	
	protected void dos2unix() {
		dos2unix(target);	
	}
	
	protected String chown(String user) {
		return chown(user, target);
	}
	
	protected String chown(String userAndGroup, boolean recursive) {
		return chown(userAndGroup, target, recursive);
	}
	
	protected String ln(String link) {
		return ln(link, target);
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