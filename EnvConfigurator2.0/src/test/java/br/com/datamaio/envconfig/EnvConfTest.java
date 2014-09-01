package br.com.datamaio.envconfig;

import static java.nio.file.Files.exists;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import br.com.datamaio.fwk.io.FileUtils;
import br.com.datamaio.fwk.io.PathUtils;


public class EnvConfTest {
	@Test
	public void testDelete() throws Exception {
		String fsResource = "/fs/fs1";
		String moduleResource = "/modules/del1";
		
		Path[] paths = createEnv(fsResource, moduleResource);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir2")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(false));
		
		EnvConf conf = new EnvConf(new ExternalConf(), module);		
		conf.deleteFiles();

		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(false));
		
		FileUtils.delete(root);
	}

	private Path[] createEnv(String fsResource, String moduleResource) throws IOException, URISyntaxException {
		Path root = Files.createTempDirectory("root");		
		Path fs = FileUtils.createDirectories(PathUtils.get(root, "fs"));
		Path module = FileUtils.createDirectories(PathUtils.get(root, "module"));
		Path completeModule = FileUtils.createDirectories(PathUtils.get(module, fs));
		
		FileUtils.copy(Paths.get(getClass().getResource(fsResource).toURI()), fs);
		FileUtils.copy(Paths.get(getClass().getResource(moduleResource).toURI()), completeModule);
		
		return new Path[]{root, fs, module};
	}
}
