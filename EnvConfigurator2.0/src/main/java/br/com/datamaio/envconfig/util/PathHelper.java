package br.com.datamaio.envconfig.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.datamaio.envconfig.conf.Configuration;

public final class PathHelper {
	private Map<String, String> properties;
	private Path module;

	public PathHelper(Configuration conf){
		this(conf.getProperties(), conf.getModuleDir());
	}
	
	PathHelper(Map<String, String> properties, Path module){
		this.properties = properties;
		this.module = module;
	}
	
	public final Path getTargetWithoutSuffix(Path path, String sufix) {
		// remove o .delete do final
		String fileName = path.getFileName().toString();
		fileName = fileName.replaceAll(sufix, "");
		path = path.resolveSibling(fileName);
		return getTarget(path);
	}
	
	public final Path getTarget(Path path) {
		// pega o arquivo destino
		final Path relativized = this.module.relativize(path);
		final Path resolved = Paths.get("/").resolve(relativized);

		// resolve as variaveis de diretório
    	String str = replacePathVars(resolved.toString());
    	return Paths.get(str);
    }

	protected String replacePathVars(String srcPath) {
		final Pattern p = Pattern.compile("@([^@])*@");
    	final Matcher m = p.matcher(srcPath);
    	while(m.find()) {
    		final String key = m.group();
			final String value = (String) properties.get(key.replaceAll("@", ""));
			if(value!=null)
				srcPath = srcPath.replace(key, value);
			else 
				throw new IllegalStateException("Variavel " + key + " não foi declarada nas configuracoes. " +
						"Utilize ExternalConf ou System.properties");
    	}
		return srcPath;
	}
}
