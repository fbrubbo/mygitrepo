package br.com.datamaio.envconfig.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import br.com.datamaio.envconfig.ExternalConf;
import br.com.datamaio.fwk.io.PathUtils;

public class VariablePathUtilsTest {
	ExternalConf conf;
	
	@Before
	public void before() {
		conf = new ExternalConf();
		conf.put("all", "VAL_all");
		conf.put("begin", "VAL_begin");
		conf.put("end", "VAL_end");
		conf.put("manydirs", "a/b/c");
	}

	@Test
	public void replacePathVars() throws IOException{		
		VariablePathHelper vpu = new VariablePathHelper(conf, null);
		assertThat(vpu.replacePathVars("/opt/jboss/@all@"), is("/opt/jboss/VAL_all"));
		assertThat(vpu.replacePathVars("/opt/jboss/@all@/test"), is("/opt/jboss/VAL_all/test"));
		assertThat(vpu.replacePathVars("/opt/jboss/@begin@test"), is("/opt/jboss/VAL_begintest"));
		assertThat(vpu.replacePathVars("/opt/jboss/test@end@"), is("/opt/jboss/testVAL_end"));
		assertThat(vpu.replacePathVars("/opt/@manydirs@/test"), is("/opt/a/b/c/test"));
	}
	
	@Test(expected=IllegalStateException.class)
	public void variableDoNotExists() throws IOException{		
		VariablePathHelper vpu = new VariablePathHelper(conf, null);
		assertThat(vpu.replacePathVars("/opt/jboss/@NOTREPLACED@/test"), is("/opt/jboss/@NOTREPLACED@/test"));
	}

	
	@Test
	public void getTarget() throws IOException{
		Path modules = Paths.get("/location/modules/mymodule");		
		Path dir = PathUtils.get(modules, "@manydirs@/test@end@");
				
		VariablePathHelper vpu = new VariablePathHelper(conf, modules);
		Path result = vpu.getTarget(dir);
		assertThat(result, is(PathUtils.get("/a/b/c/testVAL_end")));		
	}
	
	@Test
	public void getTargetWithoutSuffix() throws IOException{
		Path modules = Paths.get("/location/modules/mymodule");		
		Path file = PathUtils.get(modules, "@manydirs@/test.txt.delete");
				
		VariablePathHelper vpu = new VariablePathHelper(conf, modules);
		Path result = vpu.getTargetWithoutSuffix(file, ".delete");
		assertThat(result, is(PathUtils.get("/a/b/c/test.txt")));		
	}
}
