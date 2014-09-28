package br.com.datamaio.envconfig.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import br.com.datamaio.envconfig.conf.Configuration;
import br.com.datamaio.fwk.io.PathUtils;

public class LogHelper
{
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final String LOG_FOLDER = "log";
	private final Configuration conf;

	public LogHelper(Configuration conf) {
		this.conf = conf;
	}

	public LogHelper startup() {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %1$tb %1$td, %1$tY %1$tH:%1$tM:%1$tS %5$s%6$s%n");

		// avoid tha the handler be registred more than once
		Handler[] handlers = LOGGER.getHandlers();
		for (Handler handler : handlers) {
			if (handler instanceof FileHandler) {
				// it is required to close in order to avoid .lck file
				FileHandler fh = (FileHandler) handler;
				fh.close();
			}
			LOGGER.removeHandler(handler);
		}

        // registry the handlers
        LOGGER.setLevel(Level.INFO);
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(consoleHandler);

		if (enableFileHandler()) {
			Path file = buildLogPath();
			String logFileName = file.toString();
			try {
				if (!Files.exists(file.getParent())) {
					Files.createDirectories(file.getParent());
				}

				FileHandler fileHandler = new FileHandler(file.toString());
				fileHandler.setFormatter(new SimpleFormatter());
				LOGGER.addHandler(fileHandler);
			} catch (Exception e) {
				throw new RuntimeException("Erro criarndo arquivo de log " + logFileName, e);
			}
		}
        
        return this;
    }
    
	private boolean enableFileHandler() {
		// set this to false during unit testing
		return Boolean.valueOf(System.getProperty("br.com.datamaio.envconfig.util.LogHelper.EnableFileHandler", "true"));
	}
    
	private Path buildLogPath() {
		try {
			Path baseDir = new java.io.File(".").getCanonicalFile().toPath();

			String moduleName = conf.getModuleDir().getFileName().toString();
			String fileName = buildFileName(baseDir);
			return PathUtils.get(baseDir, LOG_FOLDER, moduleName, fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String buildFileName(Path baseDir) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMHHmmss");
		
		String fileName = baseDir.relativize(conf.getConfigPath()).toString().replace("config/", "");
		if(fileName.endsWith(".conf")) {
			fileName = fileName.replace(".conf", "");
		}
		if(fileName.endsWith(".properties")) {
			fileName = fileName.replace(".properties", "");
		}
		return fileName + "_" + df.format(new Date()) + ".log";
	}
}

