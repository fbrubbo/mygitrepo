package br.com.datamaio.env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.datamaio.env.util.EncodingHelper;
import br.com.datamaio.env.util.Encryptor;

public class ExternalConf extends Properties {
	private static final EncodingHelper encodingHelper = new EncodingHelper();
	private static Pattern p = Pattern.compile("\\$\\{([^\\$\\{])*\\}");
	
	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		merge();
	}
	
	@Override
	public synchronized void load(Reader reader) throws IOException {
		super.load(reader);
		merge();
	}
	
	@Override
	public synchronized void loadFromXML(InputStream in) throws IOException,
			InvalidPropertiesFormatException {
		super.loadFromXML(in);
		merge();
	}
	
	public synchronized void load(final Path conf) {
        if (conf!=null) {
            // primeiro carrega do arquivo
        	final Charset cs = encodingHelper.getCharset(conf.toString());
    		try (BufferedReader reader = Files.newBufferedReader(conf, cs)) {
    			super.load(reader);
    		} catch (IOException e) {
    			throw new RuntimeException(e);
    		}
    		merge();
        }
	}

	private void merge() {
		// faz este loop apenas para garantir que, se tiver alguma propriedade criptografada, ela sera descriptografada
		// também resolve as vari�veis
		final Set<Object> propsKeySet = keySet();
		for (final Object key : propsKeySet) {
			final String value = (String) get(key);
			putInProps(key, value);
		}

		// pega todas as propriedades do sistema e sobreescreve as configuradas no arquivo de propriedades
		final Properties sysProps = System.getProperties();
		final Set<Object> sysPropsKeySet = sysProps.keySet();
		for (final Object key : sysPropsKeySet) {
			final String value = (String) sysProps.get(key);
			putInProps(key, value);
		}
	}
	
	private void putInProps(final Object key, final String value) {
        // para as propriedades cryptografadas, decriptografa..
		if(value!=null && value.startsWith(Encryptor.PREFIX)) {
			final String decryptedValue = Encryptor.get().decrypt(value);
			put(key, decryptedValue);
		} else {
			put(key, resolvePropertyValue(value));
		}
	}
	
	public String resolvePropertyValue(String value) {
        final Matcher m = p.matcher(value);
        while(m.find()) {
            final String key = m.group();
            final String keyProp = key.substring(0, key.length()-1).substring(2);

            String innerValue = System.getProperties().getProperty(keyProp);
            if(innerValue==null) {
                innerValue = getProperty(keyProp);
            }

            if(innerValue!=null){
                final Matcher m2 = p.matcher(innerValue);
                if(m2.find()){
                    innerValue = resolvePropertyValue(innerValue);
                }
                value = value.replace(key, innerValue);
            }
        }

        return value;
    }
}
