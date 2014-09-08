package br.com.datamaio.envconfig.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.com.datamaio.envconfig.conf.Configuration;

public class LogHelper
{
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String LOG_FOLDER = "log";
    private final Configuration conf;

    public LogHelper(Configuration conf)
    {
        this.conf = conf;
    }

    public LogHelper setup()
    {
    	System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %1$tb %1$td, %1$tY %1$tH:%1$tM:%1$tS %5$s%6$s%n");
    	
        // evita que os handlers sejam registrados mais de uma vez
        Handler[] handlers = LOGGER.getHandlers();
        for(Handler handler : handlers) {
            if(handler instanceof FileHandler){
                // é necessário fechar o arquivo antes de remover para evitar que ele crie um .lck
                FileHandler fh = (FileHandler) handler;
                fh.close();
            }
            LOGGER.removeHandler(handler);
        }

        // registra novamente os handlers.. tem que fazer isto pois o nome do arquivo é setado ai em baixo
        LOGGER.setLevel(Level.INFO);
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(consoleHandler);

        if(enableFileHandler()) {
	        Path file = buildLogPath();
	        String logFileName = file.toString();
	        try
	        {
	            if(!Files.exists(file.getParent()))
	            {
	                Files.createDirectories(file.getParent());
	            }
	
	            FileHandler fileHandler = new FileHandler(file.toString());
	            fileHandler.setFormatter(new SimpleFormatter());
	            LOGGER.addHandler(fileHandler);
	        }
	        catch(Exception e)
	        {
	            throw new RuntimeException("Erro criarndo arquivo de log " + logFileName, e);
	        }
        }
        
        return this;
    }
    
	public void printProperties() {
		LOGGER.info("==============================================================================================================");
		LOGGER.info("=============== PROPERTIES QUE PODEM SER USADAS NOS ARQUIVOS COM LOGICA (*.hook OU *.tmpl) ===================");
		Map<String, String> props = conf.getInstalationProperties();
		Set<String> keys = props.keySet();
		for (String key : keys) {
			LOGGER.info(String.format("%s = %s", key, props.get(key)));
		}
	}

	private boolean enableFileHandler() {
		return Boolean.valueOf(System.getProperty("br.com.datamaio.envconfig.util.LogHelper.EnableFileHandler", "true"));
	}
    
    private Path buildLogPath()
    {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMHHmmss");
		try {
			File currentDir = new java.io.File(".").getCanonicalFile();

			String moduleName = conf.getInstalationModule().getFileName().toString();
			String fileName = currentDir.toPath().relativize(conf.getInstalationPropertiesPath()).toString();
			fileName = fileName.substring(0, fileName.indexOf(".properties") < 0 ? fileName.length() : fileName.indexOf(".properties"));
			String log = currentDir + "/" + LOG_FOLDER + "/" + moduleName + "/" + fileName + "_" + df.format(new Date()) + ".log";
			return Paths.get(log);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}

