package br.com.datamaio.fwk.io;

import static java.nio.file.Files.exists;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class ZipUtilsTest {
	@Test
	public void unzip() throws Exception {		
		URL url = ZipUtilsTest.class.getResource("/br/com/datamaio/fwk/io/zip2test.zip");
		Path zipFile = Paths.get(url.toURI());
		Path targetDir = Files.createTempDirectory("DIR");
		
		ZipUtils.unzip(zipFile, targetDir);	
		Path img = PathUtils.get(targetDir, "/dir/subdir/img.jpg");
		assertThat(exists(img), is(true));
		
		// cleanup		
		FileUtils.delete(targetDir);
	}
	
	@Test
	public void zip() throws Exception {		
		Path dir = Files.createTempDirectory("DIR");
		Path file = Files.createTempFile(dir, "TEMP", ".txt");
		Path targetDir = Files.createTempDirectory("TARGET");
		Path zipFile = PathUtils.get(targetDir, "my.zip");
		
		ZipUtils.create(zipFile, dir, file);	
		assertThat(exists(zipFile), is(true));
		List<String> list = ZipUtils.list(zipFile);
		assertThat(list.size(), is(1));
		assertThat(list, hasItem(endsWith(file.getFileName().toString())));
		
		// cleanup
		FileUtils.delete(dir);
		FileUtils.delete(targetDir);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void zipWithIllegalParameters() throws IOException {
		Path dir = Files.createTempDirectory("DIR");
		Path dir2 = Files.createTempDirectory("DIR2");
		Path file2 = Files.createTempFile(dir2, "TEMP", ".txt");

		try {	
			Path zipFile = Paths.get("my.zip");
			ZipUtils.create(zipFile, dir, file2);	
		} finally {
			// cleanup
			FileUtils.delete(dir);
			FileUtils.delete(dir2);
		}
	}
	
	@Test
	public void zipDir() throws IOException {
		Path dir = Files.createTempDirectory("DIR");
		Path file = Files.createTempFile(dir, "TEMP", ".txt");
		Path subDir = Files.createTempDirectory(dir, "SUB_DIR");
		Path subFile = Files.createTempFile(subDir, "TEMP", ".txt");
		Path targetDir = Files.createTempDirectory("TARGET");
		Path zipFile = PathUtils.get(targetDir, "my.zip");
		
		ZipUtils.create(zipFile, dir);	
		assertThat(exists(zipFile), is(true));
		List<String> list = ZipUtils.list(zipFile);
		assertThat(list.size(), is(3));
		assertThat(list, hasItem(endsWith(file.getFileName().toString())));
		assertThat(list, hasItem(endsWith(subFile.getFileName().toString())));
		
		// cleanup
		FileUtils.delete(dir);
		FileUtils.delete(targetDir);
	}
}
