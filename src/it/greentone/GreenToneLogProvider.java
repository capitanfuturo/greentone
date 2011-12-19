package it.greentone;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
public class GreenToneLogProvider
{
	Logger logger;
	private static final String LOG_FILENAME = "log.txt";
	private FileHandler fileHandler;

	/**
	 * Crea e configura il sistema di logging dell'applicazione.
	 */
	public GreenToneLogProvider()
	{
		logger = Logger.getLogger("it.greentone");
		try
		{
			fileHandler =
			  new FileHandler(GreenToneAppConfig.BASE_PATH + LOG_FILENAME, true);
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Restituisce il logger applicativo.
	 * 
	 * @return il logger applicativo
	 */
	public Logger getLogger()
	{
		return logger;
	}

	/**
	 * Elimina i log del programma.
	 */
	public void deleteLogs()
	{
		logger.removeHandler(fileHandler);
		File logFile = new File(GreenToneAppConfig.BASE_PATH + LOG_FILENAME);
		if(logFile.exists() && logFile.canWrite())
			logFile.delete();
	}
}
