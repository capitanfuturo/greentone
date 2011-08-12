package it.greentone.persistence;

import it.greentone.GreenTone;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

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
 * Enumerato che descrive lo stato di un incarico.
 * 
 * @author Giuseppe Caliendo
 */
public enum JobStatus
{
	/** Incarico in pianificazione */
	PLANNING
	{
		@Override
		public String getLocalizationKey()
		{
			return "Enum.JobStatus.Planning";
		}
	},
	/** Incarico in lavorazione */
	WORKING
	{
		@Override
		public String getLocalizationKey()
		{
			return "Enum.JobStatus.Working";
		}
	},
	/** Incarico sospeso */
	SUSPEND
	{
		@Override
		public String getLocalizationKey()
		{
			return "Enum.JobStatus.Suspend";
		}
	},
	/** Incarico chiuso */
	CLOSED
	{
		@Override
		public String getLocalizationKey()
		{
			return "Enum.JobStatus.Closed";
		}
	};

	ResourceMap resourceMap;

	private JobStatus()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Restituisce la chiave per la localizzazione dell'enumerato.
	 * 
	 * @return la chiave per la localizzazione dell'enumerato
	 */
	public abstract String getLocalizationKey();

	/**
	 * Restituisce la localizzazione dell'enumerato.
	 * 
	 * @return la localizzazione dell'enumerato
	 */
	public String getLocalizedName(){
		return resourceMap.getString(getLocalizationKey());
	}
}
