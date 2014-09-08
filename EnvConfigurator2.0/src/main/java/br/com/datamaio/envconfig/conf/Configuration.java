package br.com.datamaio.envconfig.conf;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

	public static final String HOOK_SUFFIX = ".hook";
	public static final String DELETE_SUFFIX = ".del";
	public static final String TEMPLATE_SUFFIX = ".tmpl";
		
	/** Contém as propriedades configuradas para a instalação */
	private Path instalationPropertiesPath;
	private final Map<String, String> instalationProperties;
	/** Contém o diretório onde está o módulo que será instalado */
	private Path instalationModule;
	/** Contém os ips dos ambientes */
	private Environments environments;
	/** Normalmente * key é o groovy dependency, value é o File onde se encontra esta dependência */
	private Map<String, File> dependencies;
	
	public Configuration(Path properties, Path module) {
		this(properties, module, new Environments(), new HashMap<>());
	}
	
	public Configuration(Path properties, Path module, Environments environments, Map<String, File> dependencies) {
		this(properties, toMaps(properties), module, environments, dependencies);
	}

	private static Map<String, String> toMaps(Path properties) {
		InstalationProperties props = new InstalationProperties().load(properties);
		return props.entrySet().stream()
				.collect(toMap(e -> e.getKey().toString()
							  ,e -> e.getValue().toString()));
	}
	
	public Configuration(Path propertiesPath, Map<String, String> properties, Path module) {
		this(propertiesPath, properties, module, new Environments(), new HashMap<>());
	}
	
	public Configuration(Path propertiesPath, Map<String, String> properties, Path module, Environments environments, Map<String, File> dependencies) {
		this.instalationPropertiesPath = propertiesPath;
		this.instalationProperties = properties;
		this.instalationModule = module;
		this.environments = environments;
		this.dependencies = dependencies;
	}


	public Path getInstalationPropertiesPath() {
		return instalationPropertiesPath;
	}

	public Map<String, String> getInstalationProperties() {
		return instalationProperties;
	}

	public Path getInstalationModule() {
		return instalationModule;
	}

	public Environments getEnvironments() {
		return environments;
	}

	public File getDependency(String name) {
		return dependencies.get(name);
	}

}
