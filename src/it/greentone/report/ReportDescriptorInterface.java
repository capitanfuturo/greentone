package it.greentone.report;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

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
 * Descrive un report di GreenTone.
 * 
 * @author Giuseppe Caliendo
 */
public interface ReportDescriptorInterface
{

	/**
	 * Esegue l'inizializzazione del report prima che venga eseguito.
	 * 
	 * @param commonParameters
	 *          input da passare al report
	 */
	public void setup(Map<String, Object> commonParameters);

	/**
	 * Restituisce l'insieme dei dati da lavorare.
	 * 
	 * @return il dataset del report
	 */
	public Collection<?> getDataSet();

	/**
	 * Restituisce una mappa dei parametri di esecuzione del report.
	 * 
	 * @return una mappa dei parametri di esecuzione del report
	 */
	public Map<String, Object> getParams();

	/**
	 * Restituisce uno stream verso il sorgente del report.
	 * 
	 * @return uno stream verso il sorgente del report
	 */
	public InputStream getReportInputStream();

	/**
	 * Restituisce l'estensione del report.
	 * 
	 * @return l'estensione del report
	 */
	public ExtensionType getExtensionType();

	/**
	 * Restituisce il nome del report.
	 * 
	 * @return il nome del report
	 */
	public String getName();

	/**
	 * Enumerato delle estensioni dei file supportati dal motore di reportistica
	 * di GreenTone
	 * 
	 * @author Giuseppe Caliendo
	 */
	public enum ExtensionType
	{
		/**
		 * Portable Document Format
		 */
		PDF
		{

			@Override
			public String getExtension()
			{
				return "pdf";
			}

		},
		/** eXtensible Markup Language */
		XML
		{
			@Override
			public String getExtension()
			{
				return "xml";
			}
		},
		/** HyperText Markup Language */
		HTML
		{
			@Override
			public String getExtension()
			{
				return "html";
			}
		};

		/**
		 * Restituisce l'estensione del report.
		 * 
		 * @return l'estensione del report
		 */
		public abstract String getExtension();
	}
}
