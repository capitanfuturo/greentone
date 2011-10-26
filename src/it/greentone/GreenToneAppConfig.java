package it.greentone;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

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
 * Bean di configurazione tra Datanucleus e Spring framework.
 * 
 * @author Giuseppe Caliendo
 */
@Configuration
public class GreenToneAppConfig
{
	/**
	 * Path del file di configurazione di Spring Framework
	 */
	public static final String SPRING_CONFIG_LOCATION =
	  "classpath:/it/greentone/resources/springAppConfig.xml";
	/**
	 * Path del file di configurazione di Datanucleus
	 */
	public static final String DATANUCLEUS_CONFIG_LOCATION =
	  "/it/greentone/resources/datanucleus.properties";
	/**
	 * Path del repository dove salvare la copia dei file associati a dei
	 * documenti
	 */
	public static final String DOCUMENTS_REPOSITORY = System
	  .getProperty("user.dir") + "/documents/";

	/**
	 * Resituisce la factory di gestione della persistenza.
	 * 
	 * @return la factory di gestione della persistenza
	 */
	@Bean
	PersistenceManagerFactory pmf()
	{

		final Properties defaults = new Properties();
		try
		{
			defaults
			  .load(getClass().getResourceAsStream(DATANUCLEUS_CONFIG_LOCATION));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		final Properties props = new Properties(defaults);
		final StringBuilder connectionURL = new StringBuilder();
		connectionURL.append("jdbc:h2:");
		/* il db è posizionato nella cartella di esecuzione di greentone */
		final String storageDirPath = System.getProperty("user.dir") + "/db";
		if(storageDirPath != null && storageDirPath.length() > 0)
		{
			connectionURL.append(storageDirPath);
			if(!storageDirPath.endsWith("/"))
			{
				connectionURL.append("/");
			}
		}
		new File(storageDirPath).mkdirs();
		connectionURL.append("GT_CAT;DB_CLOSE_ON_EXIT=TRUE");
		props.put("javax.jdo.option.ConnectionURL", connectionURL.toString());
		props.put("javax.jdo.option.ConnectionUserName", "user");
		props.put("javax.jdo.option.ConnectionPassword", "");
		return JDOHelper.getPersistenceManagerFactory(props);
	}

	/**
	 * Restituisce il manager delle transazioni.
	 * 
	 * @return il manager delle transazioni
	 */
	@Bean
	public PlatformTransactionManager txManager()
	{
		return new JdoTransactionManager(pmf());
	}

}
