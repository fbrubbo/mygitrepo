package br.com.datamaio.envconfig;

import static java.nio.file.Files.exists;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.datamaio.fwk.io.FileUtils;
import br.com.datamaio.fwk.io.PathUtils;


public class EnvConfiguratorTest {
	
	@Before
	public void setup(){
		System.setProperty("br.com.datamaio.envconfig.util.LogHelper.EnableFileHandler", "false");
	}
	
	@Test
	public void testDelete() throws Exception {
		Path[] paths = createEnv(1);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir2")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(false));
		
		new EnvConfigurator(new HashMap<>(), module).deleteFiles();

		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(false));
		
		FileUtils.delete(root);
	}

	@Test
	public void testSimpleCopy() throws Exception {
		Path[] paths = createEnv(2);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2/f2.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(true));
		
		new EnvConfigurator(new HashMap<>(), module).copyFiles();		

		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(true));
		
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));

		
		FileUtils.delete(root);
	}
	
	@Test
	public void testComplexCopy() throws Exception {
		Path[] paths = createEnv(3);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		Path result = paths[3];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2/f2.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(true));
		
		Map<String, String> ext = new HashMap<>();
		ext.put("favlang", "aaaaaa");
		ext.put("favlang2", "bbbbbb");
		new EnvConfigurator(ext, module).copyFiles();		

		checkResult(fs, result, "dir1/f1.txt");
		checkResult(fs, result, "dir2/dir21/f21.txt");
		checkResult(fs, result, "f.txt");
		checkResult(fs, result, "ff.txt");
		checkResult(fs, result, "dir3/f3.txt");

		FileUtils.delete(root);
	}
	

	@Test
	public void testExec() throws Exception {
		Path[] paths = createEnv(4);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		Path result = paths[3];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir2/f2.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir1/f1.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(true));
		
		Map<String, String> ext = new HashMap<>();
		ext.put("favlang", "aaaaaa");
		ext.put("favlang2", "bbbbbb");
		new EnvConfigurator(ext, module).exec();		

		checkResult(fs, result, "dir1/f1.txt");
		checkResult(fs, result, "dir2/dir21/f21.txt");
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(false));
		checkResult(fs, result, "f.txt");
		checkResult(fs, result, "ff.txt");
		
		FileUtils.delete(root);
	}
	
	@Test
	public void testExecWithPreCondition() throws Exception {
		Path[] paths = createEnv(5);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		
		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		
		Map<String, String> ext = new HashMap<>();
		ext.put("favlang", "aaaaaa");
		new EnvConfigurator(ext, module).exec();		

		assertThat(exists(PathUtils.get(fs, "dir2/dir21/f21.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		
		FileUtils.delete(root);
	}
	
	@Test
	public void testExecWithPostCondition() throws Exception {
		Path[] paths = createEnv(6);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		
		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));		
		Set<PosixFilePermission> before = Files.getPosixFilePermissions(PathUtils.get(fs, "f.txt"));
		assertThat(exists(PathUtils.get(fs, "ff.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "ff.txt.postexecuted")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		
		Map<String, String> ext = new HashMap<>();
		ext.put("favlang", "aaaaaa");
		ext.put("favlang2", "bbbbbb");
		new EnvConfigurator(ext, module).exec();		

		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		Set<PosixFilePermission> after = Files.getPosixFilePermissions(PathUtils.get(fs, "f.txt"));
		assertThat(after, is(not(equalTo(before))));
		assertThat(exists(PathUtils.get(fs, "ff.txt.postexecuted")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt.postexecuted")), is(true));
		
		FileUtils.delete(root);
	}
	
	@Test
	public void testExecWithExecPreCondition() throws Exception {
		Path[] paths = createEnv(7);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		Files.write(PathUtils.get(module, "Module.hook"), buildModuleHookPre()); 
				
		Map<String, String> ext = new HashMap<>();
		ext.put("var", "var errada");
		new EnvConfigurator(ext, module).exec();		

		assertThat(exists(PathUtils.get(fs, "f.txt")), is(false));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(false));
		
		FileUtils.delete(root);
	}
	
	@Test
	public void testExecWithExecPostCondition() throws Exception {
		Path[] paths = createEnv(8);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		Files.write(PathUtils.get(module, "Module.hook"), buildModuleHookPost()); 
				
		Map<String, String> ext = new HashMap<>();
		new EnvConfigurator(ext, module).exec();		

		assertThat(exists(PathUtils.get(fs, "f.txt")), is(true));
		assertThat(exists(PathUtils.get(fs, "dir3/f3.txt")), is(true));
		assertThat(exists(PathUtils.get(module, "Module.postexecuted")), is(true));		
		
		FileUtils.delete(root);
	}
	
	/**
	 * Make sure the behavior will not be changed in the future. 
	 *    Issue: https://jira.codehaus.org/browse/GROOVY-2939
	 *    Because of this issue the creators do not want to fix, we need to encode in .tmpl files:
	 *      - all '\' as '\\' 
	 *      - all '$' as '\$' (not because of the issue, but because it is a char to execute the EL)  
	 * 
	 * If we change this, it should be configurable
	 */
	@Test
	public void testComplexTmplFiles() throws Exception {
		Path[] paths = createEnv(9);
		Path root = paths[0];
		Path fs = paths[1];
		Path module = paths[2];
		Path result = paths[3];
		
		assertThat(exists(PathUtils.get(fs, "f1.txt")), is(false));
		
		Map<String, String> ext = new HashMap<>();
		new EnvConfigurator(ext, module).exec();		

		assertThat(exists(PathUtils.get(fs, "f1.txt")), is(true));
		checkResult(fs, result, "f1.txt");
		
		FileUtils.delete(root);
	}
	
	private byte[] buildModuleHookPre() {
		return ("println envs; println modulePath; println conf; println props;"
				+ "\nboolean pre() {"
				+ "\n	return \"xyz\".equals(get(\"var\"));"
				+ "\n}").getBytes();
	}
	
	private byte[] buildModuleHookPost() {
		return ("\nimport java.nio.file.Files;"
				+ "\nimport java.nio.file.Paths;"
		        + "\n"
				+ "\nvoid post() {"
				+ "\n	Files.createFile(Paths.get(moduleDir + \"/Module.postexecuted\"));"
				+ "\n}").getBytes();
	}

	private Path[] createEnv(int index) throws IOException, URISyntaxException {
		Path root = Files.createTempDirectory("root");		
		Path fs = FileUtils.createDirectories(PathUtils.get(root, "fs"));
		Path module = FileUtils.createDirectories(PathUtils.get(root, "module"));
		Path result = FileUtils.createDirectories(PathUtils.get(root, "result"));
		Path completeModule = FileUtils.createDirectories(PathUtils.get(module, fs));
		
		FileUtils.copy(Paths.get(getClass().getResource("/fs/fs" + index).toURI()), fs);
		FileUtils.copy(Paths.get(getClass().getResource("/module/m" + index).toURI()), completeModule);
		FileUtils.copy(Paths.get(getClass().getResource("/result/r" + index).toURI()), result);
		
		return new Path[]{root, fs, module, result};
	}
	private void checkResult(Path fs, Path result, String file) {
		assertThat(exists(PathUtils.get(fs, file)), is(true));
		try {
			byte[] actual = Files.readAllBytes(PathUtils.get(fs, file));
			byte[] expected = Files.readAllBytes(PathUtils.get(result, file));
			Assert.assertThat(actual, is(equalTo(expected)));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
