package br.com.datamaio.envconfig.hooks.file;

import static br.com.datamaio.envconfig.conf.Configuration.HOOK_SUFFIX;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.hooks.HookEvaluator;

public class FileHookEvaluator extends HookEvaluator {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Path src;
    
	public FileHookEvaluator(final Path src, final Path target, final Configuration conf) {
		super(Paths.get(src + HOOK_SUFFIX), buildBinding(src, target), conf);
		this.src = src;
	}

	@Override
	protected String getScriptBaseClass() {
		return FileHook.class.getName();
	}	
	
	public boolean pre(){
		LOGGER.info("INSTALLING: " + relativize() );
		LOGGER.info(" :PRE");		
		boolean pre = super.pre();
		if(!pre) {
			LOGGER.info(" :POST (Not Executed. Function 'pre' returned false)");
		}
		return pre;
	}
	
	public void post(){
		LOGGER.info(" :POST");
		super.post();
		LOGGER.info("--------------------------" );
	}
	
	private static Map<String, Object> buildBinding(final Path src, final Path target) {
		Map<String, Object> map = new HashMap<>();
		map.put("src", src.toString());
		map.put("target", target.toString());
		return map;
	}

	private String relativize() {
		String relative = src.toString();
		Path p = Paths.get(relative.substring(relative.indexOf("module" + File.separator)));
		return p.subpath(2, p.getNameCount()).toString();
	}
}
