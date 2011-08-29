package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.MainPanel;

import java.awt.Component;

import javax.swing.JTabbedPane;

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
 * Azione legata alla visualizzazione di tipi di oggetti del database nel
 * pannello principale dell'applicazione. Aggiunge una scheda se non già
 * presente nel pannello principale.
 * 
 * @author Giuseppe Caliendo
 */
public abstract class ContextualAction
{
	private final MainPanel mainPanel;
	private final ResourceMap resourceMap;
	private final String applicationName;

	/**
	 * Azione legata alla visualizzazione di tipi di oggetti del database nel
	 * pannello principale dell'applicazione. Aggiunge una scheda se non già
	 * presente nel pannello principale.
	 * 
	 * @param mainPanel
	 *          pannello principale dell'applicazione
	 */
	public ContextualAction(MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		this.applicationName = resourceMap.getString("Application.title");
	}

	protected ResourceMap getResourceMap()
	{
		return resourceMap;
	}

	protected MainPanel getMainPanel()
	{
		return mainPanel;
	}

	protected void addTab(ContextualPanel panel)
	{
		/* controllo che il tab non sia già presente */
		JTabbedPane tabbedPane = getMainPanel().getMainTabbedPane();
		boolean tabInserted = false;
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			Component componentAt = tabbedPane.getComponentAt(i);
			if(componentAt.equals(panel))
			{
				tabInserted = true;
			}
		}
		if(!tabInserted)
		{
			/* aggiorno il titolo dell'applicazione */
			Application.getInstance(GreenTone.class).getMainFrame()
			  .setTitle(panel.getPanelName() + " - " + applicationName);
			/* configuro e aggiungo il tab */
			panel.setup();
			tabbedPane.add(panel, panel.getPanelName(), 0);
		}
		tabbedPane.setSelectedComponent(panel);
	}
}
