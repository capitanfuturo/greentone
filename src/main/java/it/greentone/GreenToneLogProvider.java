package it.greentone;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code>
 * <br>
 * <br>
 * Logger del programma.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class GreenToneLogProvider {
	@Inject
	ConfigurationProperties properties;
	Logger logger;
	private FileHandler fileHandler;

	/**
	 * Restituisce il logger applicativo.
	 * 
	 * @return il logger applicativo
	 */
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("it.greentone");
			try {
				fileHandler = new FileHandler(GreenToneAppConfig.LOG_PATH, true);
				fileHandler.setLevel(properties.getLogLevel());
				fileHandler.setFormatter(new SimpleFormatter());
				logger.addHandler(fileHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	/**
	 * Elimina i log del programma.
	 * 
	 * @return <code>true</code> se i log sono stati eliminati,
	 *         <code>false</code> altrimenti
	 */
	public boolean deleteLogs() {
		logger.removeHandler(fileHandler);
		logger.info("Removing log file handler");
		fileHandler.flush();
		fileHandler.close();
		File logFile = new File(GreenToneAppConfig.LOG_PATH);
		logger.info("Log file to delete: " + GreenToneAppConfig.LOG_PATH);
		logger.info("Log file exists? : " + logFile.exists());
		logger.info("Can write log file? : " + logFile.canWrite());
		boolean result = false;
		if (logFile.exists() && logFile.canWrite()) {
			try {
				result = logFile.delete();
				if (result) {
					logger.info("Log file deleted");
				} else {
					logger.info("Can't delete log file");
				}
			} catch (Exception e) {
				logger.info("Exception " + e.getStackTrace());
			}
		}
		return result;
	}
}
