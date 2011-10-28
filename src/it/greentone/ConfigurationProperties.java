package it.greentone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
 * Configurazione utente del programma.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ConfigurationProperties
{
	final Properties properties;
	private final String PROPERTY_FILE_PATH = System.getProperty("user.home")
	  + "/GreenTone/configuration.properties";
	/** Identificativo per la property di controllo aggiornamento */
	private static final String CONF_CHECK_UPDATE = "checkUpdate";
	/**
	 * Identificativo per la property del prezzo della vacazione del
	 * professionista
	 */
	private static final String CONF_VAC_PRICE = "vacPrice";
	/** Identificativo per la property del prezzo della vacazione dell'aiutante */
	private static final String CONF_VAC_HELPER_PRICE = "vacHelperPrice";


	/**
	 * Configurazione utente del programma
	 */
	public ConfigurationProperties()
	{
		/* carico la configurazione del programma */
		properties = new Properties();

		FileInputStream in;
		try
		{
			in = new FileInputStream(PROPERTY_FILE_PATH);
			properties.load(in);
			in.close();
		}
		catch(FileNotFoundException fnfe)
		{
			// fnfe.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Salva la configurazione su file.
	 */
	public void store()
	{
		FileOutputStream out;
		try
		{
			out = new FileOutputStream(PROPERTY_FILE_PATH);
			properties
			  .store(out,
			    "Greentone - file autogenerato, non cancellare o modificare manualmente");
			out.close();
		}
		catch(FileNotFoundException e)
		{
			// e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Restituisce <code>true</code> se è attivato il controllo degli
	 * aggiornamenti, <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è attovato il controllo degli aggiornamenti,
	 *         <code>false</code> altrimenti
	 */
	public boolean isCheckUpdateActivated()
	{
		String value = properties.getProperty(CONF_CHECK_UPDATE, "true");
		return Boolean.valueOf(value).booleanValue();
	}

	/**
	 * Imposta l'attivazione del controllo degli aggiornamenti.
	 * 
	 * @param isActivated
	 *          <code>true</code> se è attovato il controllo degli aggiornamenti,
	 *          <code>false</code> altrimenti
	 */
	public void setCheckUpdateActivated(boolean isActivated)
	{
		properties.setProperty(CONF_CHECK_UPDATE, "" + isActivated);
	}

	/**
	 * Restituice il prezzo della vacazione del professionista.
	 * 
	 * @return il prezzo della vacazione del professionista
	 */
	public Double getVacazionePrice()
	{
		String value = properties.getProperty(CONF_VAC_PRICE, "44.93d");
		return Double.parseDouble(value);
	}

	/**
	 * Imposta il prezzo della vacazione del professionista.
	 * 
	 * @param price
	 *          il prezzo della vacazione del professionista
	 */
	public void setVacazionePrice(double price)
	{
		properties.setProperty(CONF_VAC_PRICE, "" + price);
	}

	/**
	 * Restituice il prezzo della vacazione dell'aiutante.
	 * 
	 * @return il prezzo della vacazione dell'aiutante
	 */
	public Double getVacazioneHelperPrice()
	{
		String value = properties.getProperty(CONF_VAC_HELPER_PRICE, "28.41d");
		return Double.parseDouble(value);
	}

	/**
	 * Imposta il prezzo della vacazione dell'aiutante.
	 * 
	 * @param price
	 *          il prezzo della vacazione dell'aiutante
	 */
	public void setVacazioneHelperPrice(double price)
	{
		properties.setProperty(CONF_VAC_HELPER_PRICE, "" + price);
	}
}
