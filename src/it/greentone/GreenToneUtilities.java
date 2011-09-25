package it.greentone;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;

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
 * Insieme di metodi di utilità grafiche e di business.
 * 
 * @author Giuseppe Caliendo
 */
public class GreenToneUtilities
{

	private static final String UPDATE_URL =
	  "http://greentone.googlecode.com/svn/trunk/installer/release.properties";
	private static final String COMMENTS_CHAR = "#";
	private static final String VERSION_SEPARATOR = ".";
	private static final String APP_NAME = "application.name";
	private static final String APP_MAJOR_VERSION = "major.version.number";
	private static final String APP_MINOR_VERSION = "minor.version.number";
	private static final String APP_MINUS_VERSION = "minus.version.number";

	/**
	 * Restituisce il testo contenuto nel campo di testo passato in ingresso. Tale
	 * testo viene ripulito di spazi iniziali e finali del campo passato in
	 * ingresso. Restituisce <code>null</code> se il testo è vuoto.
	 * 
	 * @param textField
	 *          il campo da processare
	 * @return il testo contenuto nel campo di testo passato in ingresso. Tale
	 *         testo viene ripulito di spazi iniziali e finali del campo passato
	 *         in ingresso. Restituisce <code>null</code> se il testo è vuoto
	 */
	public static String getText(JTextComponent textField)
	{
		String tmp = textField.getText();
		if(tmp != null)
		{
			tmp = tmp.trim();
			if(tmp.length() == 0)
			{
				tmp = null;
			}
		}
		return tmp;
	}

	/**
	 * Restituisce una data di tipo Joda Time a partire dal widget di Swingx.
	 * 
	 * @param datePicker
	 *          il componente grafico
	 * @return la data di tipo Joda Time
	 */
	public static DateTime getDateTime(JXDatePicker datePicker)
	{
		Date date = datePicker.getDate();
		return date != null? new DateTime(date): null;
	}

	/**
	 * Restituisce un formatter per il pattern passato in ingresso. A differenza
	 * della classe fornita da Java questo formatter accetta anche valori
	 * <code>null</code>
	 * 
	 * @param mask
	 *          pattern di formattazione
	 * @return un formatter per il pattern passato in ingresso
	 * @see MaskFormatter
	 */
	public static MaskFormatter createMaskFormatter(String mask)
	{
		MaskFormatter mf = null;
		try
		{
			mf = new MaskFormatter(mask)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Object stringToValue(String value) throws ParseException
					{
						if(value == null || value.length() == 0 || value.trim().isEmpty())
						{
							return null;
						}
						return super.stringToValue(value);
					}
				};
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return mf;
	}

	/**
	 * Restituisce la stringa della nuova versione disponibile, <code>null</code>
	 * altrimenti.
	 * 
	 * @return la stringa della nuova versione disponibile, <code>null</code>
	 *         altrimenti
	 */
	public static String checkUpdates()
	{
		ResourceMap resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		String currentVersion = resourceMap.getString("Application.version");
		String remoteVersion = "";
		try
		{
			/* carico il contenuto dal file in remoto */
			BufferedInputStream in =
			  new BufferedInputStream(new URL(UPDATE_URL).openStream());

			StringBuffer strBuffer = new StringBuffer();
			byte data[] = new byte[1024];
			while(in.read(data, 0, 1024) >= 0)
			{
				for(int i = 0; i < data.length; i++)
				{
					strBuffer.append((char) data[i]);
				}
			}
			in.close();
			/* faccio il parsing dei dati caricati */
			BufferedReader reader =
			  new BufferedReader(new StringReader(strBuffer.toString()));
			String str;
			String major = "";
			String minor = "";
			String minus = "";
			while((str = reader.readLine()) != null)
			{
				/* escludo la riga di commento e la riga del nome dell'applicazione */
				if(str.length() > 0 && !str.startsWith(COMMENTS_CHAR)
				  && !str.startsWith(APP_NAME))
				{
					if(str.startsWith(APP_MAJOR_VERSION))
					{
						major = str.substring(str.length() - 1) + VERSION_SEPARATOR;
					}
					else
						if(str.startsWith(APP_MINOR_VERSION))
						{
							minor = str.substring(str.length() - 1) + VERSION_SEPARATOR;
						}
						else
							if(str.startsWith(APP_MINUS_VERSION))
							{
								minus = str.substring(str.length() - 1);
							}
				}
			}
			remoteVersion = major + minor + minus;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(remoteVersion.length() > 0)
		{
			return remoteVersion.equals(currentVersion)? null: remoteVersion;
		}
		return null;
	}
}
