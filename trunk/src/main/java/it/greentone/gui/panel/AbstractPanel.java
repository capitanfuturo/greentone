package it.greentone.gui.panel;

import it.greentone.GreenTone;

import javax.swing.JPanel;

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
 * Pannello generico da estendere per poter avere delle facilities utili.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public abstract class AbstractPanel extends JPanel
{
	private final ResourceMap resourceMap;
	private EStatus status;

	/**
	 * Pannello generico da estendere per poter avere delle facilities utili.
	 */
	public AbstractPanel()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	protected ResourceMap getResourceMap()
	{
		return resourceMap;
	}

	/**
	 * Inizializza o re-inizializza il pannello intero
	 */
	public void setup()
	{
		setStatus(EStatus.NEW);
	}

	/**
	 * Restituisce il nome del bundle del pannello.
	 * 
	 * @return il nome del bundle del pannello
	 */
	public abstract String getBundleName();

	/**
	 * Restituisce lo stato attuale del pannello contestuale.
	 * 
	 * @return lo stato attuale del pannello contestuale
	 */
	public EStatus getStatus()
	{
		return status;
	}

	/**
	 * Imposta lo stato attuale del pannello contestuale.
	 * 
	 * @param status
	 *          lo stato attuale del pannello contestuale
	 */
	public void setStatus(EStatus status)
	{
		this.status = status;
	}

	/**
	 * Stato di un pannello contestuale.
	 * 
	 * @author Giuseppe Caliendo
	 */
	public enum EStatus
	{
		/**
		 * Modalità modifica.
		 */
		EDIT,
		/**
		 * Modalità nuovo inserimento.
		 */
		NEW;
	}
}
