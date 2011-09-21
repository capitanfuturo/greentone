package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.PersonsPanel;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Elimina una persona dall'anagrafica.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class DeletePersonAction extends AbstractBean
{
	@Inject
	PersonsPanel personsPanel;
	@Inject
	PersonService personService;
	boolean deletePersonActionEnabled = false;
	private final ResourceMap resourceMap;

	/**
	 * Elimina una persona dall'anagrafica.
	 */
	public DeletePersonAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Elimina una persona dall'anagrafica.
	 */
	@Action(enabledProperty = "deletePersonActionEnabled")
	public void deletePerson()
	{
		int confirmDialog =
		  JOptionPane.showConfirmDialog(personsPanel,
		    resourceMap.getString("deletePerson.Action.confirmMessage"));
		if(confirmDialog == JOptionPane.OK_OPTION)
		{
			Person person = personsPanel.getSelectedItem();
			personService.deletePerson(person);
			personsPanel.clearForm();
			personsPanel.setStatus(EStatus.NEW);
		}
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isDeletePersonActionEnabled()
	{
		return deletePersonActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param deletePersonActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setDeletePersonActionEnabled(boolean deletePersonActionEnabled)
	{
		final boolean oldValue = this.deletePersonActionEnabled;
		this.deletePersonActionEnabled = deletePersonActionEnabled;
		firePropertyChange("deletePersonActionEnabled", oldValue,
		  deletePersonActionEnabled);
	}
}
