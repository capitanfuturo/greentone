package it.greentone.gui.action;

import it.greentone.gui.MainPanel;
import it.greentone.gui.panel.PersonPanel;
import it.greentone.persistence.Person;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2012 GreenTone Developer Team.<br>
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
 * Mostra una persona e i suoi dettagli nel pannello principale
 * dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewPersonAction extends AbstractBean
{
	@Inject
	private PersonPanel personPanel;
	private Person person;
	boolean viewPersonActionEnabled = false;
	@Inject
	private MainPanel mainPanel;

	/**
	 * Imposta la persona da visualizzare.
	 * 
	 * @param person
	 *          la persona
	 */
	public void setPerson(Person person)
	{
		this.person = person;
	}

	/**
	 * Mostra la persona nel pannello principale dell'applicazione.
	 */
	@Action(enabledProperty = "viewPersonActionEnabled")
	public void viewPerson()
	{
		personPanel.setPerson(person);
		personPanel.setup();
		ContextualAction.addTab(mainPanel, personPanel);
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isViewPersonActionEnabled()
	{
		return viewPersonActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param viewPersonActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setViewPersonActionEnabled(boolean viewPersonActionEnabled)
	{
		final boolean oldValue = this.viewPersonActionEnabled;
		this.viewPersonActionEnabled = viewPersonActionEnabled;
		firePropertyChange("viewPersonActionEnabled", oldValue,
		  viewPersonActionEnabled);
	}
}
