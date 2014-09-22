package br.com.datamaio.envconfig.hooks.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.datamaio.envconfig.hooks.Hook;


public abstract class FileHook extends Hook {
	protected String src;
	protected String target;
	
	protected String chmod(String mode) {
		return command.chmod(mode, target);
	}
	
	protected String chmod(String mode, boolean recursive) {
		return command.chmod(mode, target, recursive);
	}
	
	public void dos2unix() {
		command.dos2unix(target);	
	}
	
	
	protected String chown(String user) {
		return command.chown(user, target);
	}
	
	protected String chown(String user, boolean recursive) {
		return command.chown(user, target, recursive);
	}
	
	protected String chown(String user, String group, boolean recursive) {
		return command.chown(user, group, recursive);
	}
	
	protected String ln(String link) {
		return command.ln(target, link);
	}
		
	protected String renameTo(String to) {
        return command.mv(target, to);
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