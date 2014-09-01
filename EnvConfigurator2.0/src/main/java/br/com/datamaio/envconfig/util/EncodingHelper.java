package br.com.datamaio.envconfig.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Classe que visita dentro das configurações do eclipse para saber exatamente o encoding configurado para cada arquivo. <br>
 * Se não exisitr um encoding configura, esta classe considera o encoding padrão do SO.
 * 
 * @author Fernando Rubbo
 */
public class EncodingHelper {

	private static final Logger LOGGER = Logger.getLogger(EncodingHelper.class);

	private static final String DEFAULT_ECLIPSE_CONF = ".settings/org.eclipse.core.resources.prefs";
	private static final String ENCODING_PREFIX = "encoding/";
	private Properties props = new Properties();

	public EncodingHelper() {
		this(DEFAULT_ECLIPSE_CONF);
	}

	public EncodingHelper(String... fileNames) {
		loadEncodingFromEclipseConf(fileNames);
		printConfiguredEncodings();
	}

	public EncodingHelper(InputStream... ins) {
		loadEncodingsFromEclipseConf(ins);
		printConfiguredEncodings();
	}

	private void loadEncodingFromEclipseConf(String... fileNames) {
		try {
			for (String fileName : fileNames) {
				final File file = new File(fileName);
				if (file.exists()) {
					loadEncodingsFromEclipseConf(new BufferedInputStream(new FileInputStream(file)));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("não foi possível ler o arquivo de encode dos arquivos", e);
		}
	}

	private void loadEncodingsFromEclipseConf(InputStream... ins) {
		try {
			for (InputStream in : ins) {
				props.load(in);
			}
		} catch (Exception e) {
			throw new RuntimeException("não foi possível ler o arquivo de encode dos arquivos", e);
		}
	}

	public void printConfiguredEncodings() {
		LOGGER.info("================================ CONFIGURED ENCODINGS in ECLIPSE ==========================================");
		for (Object key : props.keySet()) {
			if (isTheKeyAnEncoding((String) key)) {
				Object value = props.get(key);
				LOGGER.info(key + " = " + value);
			}
		}
		LOGGER.info("===========================================================================================================");
	}

	public Charset getCharset(String fileName, String defaultEncoding) {
		fileName = fileName.replaceAll("\\\\", "/");

		String encode = props.getProperty(ENCODING_PREFIX + fileName);
		if (encode == null) {
			encode = props.getProperty(ENCODING_PREFIX + "/" + fileName);
		}

		if (encode == null) {
			// TODO : improve performance. once it will be executed for each
			// file merged
			for (Object _key : props.keySet()) {
				String key = (String) _key;
				if (isTheKeyAnEncoding(key)) {
					if (key.contains(fileName) || 
							fileName.contains(key.substring(ENCODING_PREFIX.length() + 1))) {
						LOGGER.debug("t\t\tFound in the inverse logic");
						encode = props.getProperty(key);
						break;
					}
				}
			}
		}

		final String result = encode == null ? defaultEncoding : encode;		
		final Charset cs = Charset.forName(result);
		LOGGER.debug("\t\tEncoding for " + fileName + " is " + cs);
		return cs;
	}

	public Charset getCharset(String fileName) {
		return getCharset(fileName, System.getProperty("file.encoding"));
	}

	private boolean isTheKeyAnEncoding(final String key) {
		return key.startsWith(ENCODING_PREFIX)
				&& (!key.equals(ENCODING_PREFIX + "<project>") && !key
						.equals(ENCODING_PREFIX + "/<project>"));
	}

}
