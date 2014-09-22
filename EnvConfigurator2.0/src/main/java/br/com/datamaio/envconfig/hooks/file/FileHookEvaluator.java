package br.com.datamaio.envconfig.hooks.file;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.envconfig.hooks.HookEvaluator;

public class FileHookEvaluator extends HookEvaluator {
	
	public FileHookEvaluator(final Path src, final Path target, final Configuration conf) {
		this(src.toFile(), target.toFile(), conf);
	}
	
	public FileHookEvaluator(final File src, final File target, final Configuration conf) {
		super(src.getAbsolutePath() + Configuration.HOOK_SUFFIX, buildBinding(src, target), conf);
	}

	@Override
	protected String getScriptBaseClass() {
		return FileHook.class.getName();
	}	
	
	private static Map<String, Object> buildBinding(final File src, final File target) {
		Map<String, Object> map = new HashMap<>();
		map.put("src", src.getAbsolutePath());
		map.put("target", target.getAbsolutePath());
		return map;
	}

}
