package it.greentone.report;

import java.util.HashMap;
import java.util.List;
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
 * Definisce il comportamento delle categorie di report.
 * 
 * @author Giuseppe Caliendo
 */
public abstract class ReportsCategoryInterface
{
	Map<String, Object> commonParameters = new HashMap<String, Object>();

	/**
	 * Restituisce la lista dei descrittori che compongono la categoria di report.
	 * 
	 * @return la lista dei descrittori che compongono la categoria di report
	 */
	public abstract List<ReportDescriptorInterface> getDescriptors();

	/**
	 * Restituisce i parametri comuni a tutti i report della categoria.
	 * 
	 * @return i parametri comuni a tutti i report della categoria
	 */
	public Map<String, Object> getCommonParameters()
	{
		return commonParameters;
	};

	/**
	 * Imposta i parametri comuni a tutti i report della categoria.
	 * 
	 * @param commonParameters
	 *          i parametri comuni a tutti i report della categoria
	 */
	public void setCommonParameters(Map<String, Object> commonParameters)
	{
		this.commonParameters = commonParameters;
	};
}
