package it.greentone.gui.action;

import it.greentone.gui.panel.MainPanel;
import it.greentone.gui.panel.PersonsPanel;

import javax.inject.Inject;

import org.jdesktop.application.Action;
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
 * Visualizza le persone presenti nel database.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewPersonsAction extends ContextualAction
{
	@Inject
	private PersonsPanel personsPanel;

	/**
	 * Azione per la visualizzazione delle persone nel pannello principale.
	 * 
	 * @param mainPanel
	 *          pannello principale
	 */
	@Inject
	public ViewPersonsAction(MainPanel mainPanel)
	{
		super(mainPanel);
	}

	/**
	 * Visualizza le persone presenti nel database.
	 */
	@Action
	public void viewPersons()
	{
		personsPanel.setup();
		addTab(personsPanel);
	}
}
