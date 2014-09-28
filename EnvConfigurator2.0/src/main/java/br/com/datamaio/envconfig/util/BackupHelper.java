package br.com.datamaio.envconfig.util;

import java.nio.file.Files;
import java.nio.file.Path;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.fwk.io.FileUtils;
import br.com.datamaio.fwk.io.PathUtils;

public class BackupHelper {
	private final Path dir;

	public BackupHelper(Configuration conf){
		this.dir = conf.getBackupDir();
	}

	public void backupFileOrDir(Path fileOrDir) {
		if(Files.exists(fileOrDir)) {
			Path bkp = PathUtils.get(dir, fileOrDir);
			if(Files.isDirectory(bkp)) {
				FileUtils.createDirectories(bkp);
			} else {
				FileUtils.createDirectories(bkp.getParent());
			}
			FileUtils.copy(fileOrDir, bkp);
		}
	}

	public void backupFile(final Path file) {
		if(Files.exists(file)) {
			Path bkp = PathUtils.get(dir, file);
			FileUtils.createDirectories(bkp.getParent());
			FileUtils.copyFile(file, bkp);
		}
	}
}
