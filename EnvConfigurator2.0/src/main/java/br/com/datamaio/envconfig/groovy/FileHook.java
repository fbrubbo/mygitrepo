package br.com.datamaio.envconfig.groovy;

import java.io.File;


public class FileHook extends Hook {
	protected File srcFile;
	protected String srcPath;
	protected File targetFile;
	protected String targetPath;
	
	protected String chmod(String mode) {
		return command.chmod(mode, targetPath);
	}
	
	protected String chmod(String mode, boolean recursive) {
		return command.chmod(mode, targetPath, recursive);
	}
	
	public void dos2unix() {
		command.dos2unix(targetPath);	
	}
	
	
	protected String chown(String user) {
		return command.chown(user, targetPath);
	}
	
	protected String chown(String user, boolean recursive) {
		return command.chown(user, targetPath, recursive);
	}
	
	protected String chown(String user, String group, boolean recursive) {
		return command.chown(user, group, recursive);
	}
	
	protected String ln(String link) {
		return command.ln(targetPath, link);
	}
		
	protected String renameTo(String to) {
        return command.mv(targetPath, to);
    }
	
	protected String getTargetDirectory(){
		if(targetFile.isDirectory()){
			return targetPath;
		}
		return targetFile.getAbsoluteFile().getParent();
	}	
}