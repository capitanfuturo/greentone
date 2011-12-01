package it.greentone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import javax.inject.Inject;

import org.jdesktop.application.Application;
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
	@Inject
	private GreenToneLogger logger;
	private boolean firstLaunch;
	final Properties properties;
	private final String PROPERTY_FILE_NAME = "configuration.properties";
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
	 * Identificativo per la property sulla modalità di generazione dei protocolli
	 * per gli incarichi
	 */
	private static final String CONF_JOB_PROT_STYLE = "useYearsInJobProtocol";
	/**
	 * Carattere utilizzato come separatore per la costruzione del protocollo di
	 * un incarico
	 */
	public static final String JOB_PROTOCOL_SEPARATOR = "-";
	/**
	 * Carattere utilizzato come separatore per la costruzione del protocollo di
	 * un documento
	 */
	public static final String DOCUMENT_PROTOCOL_SEPARATOR = "-";
	/**
	 * Numero di caratteri da utilizzare come padding di 0 nel codice del
	 * protocollo incarico parte numerica
	 */
	public static final int JOB_PROTOCOL_NUMERIC_LENGTH = 4;
	/**
	 * Numero di caratteri da utilizzare come padding di 0 nel codice del
	 * protocollo documento parte numerica
	 */
	public static final int DOCUMENT_PROTOCOL_NUMERIC_LENGHT = 5;
	/** Carattere di padding per il protocollo dell'incarico */
	public static final Character PROTOCOL_PADDING_CHAR = '0';
	/** Carattere di padding per il protocollo del documento */
	public static final Character DOCUMENT_PADDING_CHAR = '0';

	/**
	 * Configurazione utente del programma
	 */
	public ConfigurationProperties()
	{
		/* carico la configurazione del programma */
		properties = new Properties();
		firstLaunch = false;

		FileInputStream in;
		try
		{
			File fileProperty =
			  new File(GreenToneAppConfig.BASE_PATH + PROPERTY_FILE_NAME);
			if(!fileProperty.exists())
			{
				fileProperty.createNewFile();
				firstLaunch = true;
			}
			in =
			  new FileInputStream(GreenToneAppConfig.BASE_PATH + PROPERTY_FILE_NAME);
			properties.load(in);
			in.close();
		}
		catch(FileNotFoundException fnfe)
		{
			logger.getLogger().info(
			  Application.getInstance(GreenTone.class).getContext().getResourceMap()
			    .getString("ErrorMessage.configurationNotFound"));
		}
		catch(IOException e)
		{
			logger.getLogger().log(
			  Level.SEVERE,
			  Application.getInstance(GreenTone.class).getContext().getResourceMap()
			    .getString("ErrorMessage.loadConfiguration"), e);
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
			out =
			  new FileOutputStream(GreenToneAppConfig.BASE_PATH + PROPERTY_FILE_NAME);
			properties
			  .store(out,
			    "Greentone - file autogenerato, non cancellare o modificare manualmente");
			out.close();
		}
		catch(FileNotFoundException e)
		{
			logger.getLogger().warning(
			  Application.getInstance(GreenTone.class).getContext().getResourceMap()
			    .getString("ErrorMessage.configurationNotFound"));
		}
		catch(IOException e)
		{
			logger.getLogger().log(
			  Level.SEVERE,
			  Application.getInstance(GreenTone.class).getContext().getResourceMap()
			    .getString("ErrorMessage.storeConfiguration"), e);
		}
	}

	/**
	 * Restituisce <code>true</code> se è attivato il controllo degli
	 * aggiornamenti, <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è attivato il controllo degli aggiornamenti,
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
	 *          <code>true</code> se è attivato il controllo degli aggiornamenti,
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

	/**
	 * Restituisce <code>true</code> se è attivato l'uso dell'annata nel
	 * protocollo, <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è attivato l'uso dell'annata nel protocollo,
	 *         <code>false</code> altrimenti
	 */
	public boolean getUseYearsInJobsProtocol()
	{
		String value = properties.getProperty(CONF_JOB_PROT_STYLE, "true");
		return Boolean.valueOf(value).booleanValue();
	}

	/**
	 * Imposta l'attivazione del controllo degli aggiornamenti.
	 * 
	 * @param isActivated
	 *          <code>true</code> se è attivato l'uso dell'annata nel protocollo,
	 *          <code>false</code> altrimenti.
	 */
	public void setUseYearsInJobsProtocol(boolean isActivated)
	{
		properties.setProperty(CONF_JOB_PROT_STYLE, "" + isActivated);
	}

	/**
	 * Restituisce <code>true</code> se è la prima volta che viene eseguito il
	 * programma, <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è la prima volta che viene eseguito il
	 *         programma, <code>false</code> altrimenti
	 */
	public boolean isFirstLaunch()
	{
		return firstLaunch;
	}
}
