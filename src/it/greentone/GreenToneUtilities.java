package it.greentone;

import java.text.ParseException;
import java.util.Date;

import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

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
}
