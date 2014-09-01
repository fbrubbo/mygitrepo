package br.com.datamaio.envconfig.groovy;

import java.io.File;

import br.com.datamaio.envconfig.util.Util;

public class FileHook extends Hook {
	protected File srcFile;
	protected String srcPath;
	protected File targetFile;
	protected String targetPath;
	
	protected String chmod(String mode) {
		return Util.chmod(mode, targetPath);
	}
	
	protected String chmod(String mode, boolean recursive) {
		return Util.chmod(mode, targetPath, recursive);
	}
	
	public void dos2unix() {
		Util.dos2unix(targetPath);	
	}
	
	
	protected String chown(String user) {
		return Util.chown(user, targetPath);
	}
	
	protected String chown(String user, boolean recursive) {
		return Util.chown(user, targetPath, recursive);
	}
	
	protected String chown(String user, String group, boolean recursive) {
		return Util.chown(user, group, recursive);
	}
	
	protected String ln(String link) {
		return Util.ln(targetPath, link);
	}
		
	protected String renameTo(String to) {
        return Util.mv(targetPath, to);
    }
	
	protected String getTargetDirectory(){
		if(targetFile.isDirectory()){
			return targetPath;
		}
		return targetFile.getAbsoluteFile().getParent();
	}	
}