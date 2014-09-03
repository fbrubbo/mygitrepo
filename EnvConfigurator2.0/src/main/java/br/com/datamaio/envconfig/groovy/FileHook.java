package br.com.datamaio.envconfig.groovy;

import java.io.File;

import br.com.datamaio.envconfig.util.Cmd;

public class FileHook extends Hook {
	protected File srcFile;
	protected String srcPath;
	protected File targetFile;
	protected String targetPath;
	
	protected String chmod(String mode) {
		return Cmd.chmod(mode, targetPath);
	}
	
	protected String chmod(String mode, boolean recursive) {
		return Cmd.chmod(mode, targetPath, recursive);
	}
	
	public void dos2unix() {
		Cmd.dos2unix(targetPath);	
	}
	
	
	protected String chown(String user) {
		return Cmd.chown(user, targetPath);
	}
	
	protected String chown(String user, boolean recursive) {
		return Cmd.chown(user, targetPath, recursive);
	}
	
	protected String chown(String user, String group, boolean recursive) {
		return Cmd.chown(user, group, recursive);
	}
	
	protected String ln(String link) {
		return Cmd.ln(targetPath, link);
	}
		
	protected String renameTo(String to) {
        return Cmd.mv(targetPath, to);
    }
	
	protected String getTargetDirectory(){
		if(targetFile.isDirectory()){
			return targetPath;
		}
		return targetFile.getAbsoluteFile().getParent();
	}	
}