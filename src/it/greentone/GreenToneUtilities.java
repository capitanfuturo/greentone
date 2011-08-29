package it.greentone;

import javax.swing.text.JTextComponent;

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
}
