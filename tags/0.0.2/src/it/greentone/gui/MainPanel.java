package it.greentone.gui;

import it.greentone.GreenTone;
import it.greentone.gui.action.ActionProvider;

import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

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
 * Pannello principale dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class MainPanel extends JPanel
{
	private JToolBar mainToolBar;
	@Inject
	private ActionProvider actionProvider;
	private JTabbedPane mainTabbedPane;
	private JLabel statusLabel;
	private ResourceMap resourceMap;

	/**
	 * Inizializza l'interfaccia grafica.
	 */
	public void initialize()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();

		setLayout(new BorderLayout());
		add(getMainToolBar(), BorderLayout.WEST);
		add(getMainTabbedPane(), BorderLayout.CENTER);
		statusLabel = new JLabel(resourceMap.getString("MainPanel.statusBarReady"));
		add(statusLabel, BorderLayout.PAGE_END);
	}

	/**
	 * Restituisce la barra laterale con la barra dei bottoni principale.
	 * 
	 * @return la barra laterale con la barra dei bottoni principale
	 */
	public JToolBar getMainToolBar()
	{
		if(mainToolBar == null)
		{
			mainToolBar = new JToolBar();
			mainToolBar.setFloatable(false);
			mainToolBar.add(actionProvider.getViewJobs());
			mainToolBar.add(actionProvider.getViewOperations());
			mainToolBar.add(actionProvider.getViewDocuments());
			mainToolBar.add(actionProvider.getViewPersons());
			mainToolBar.add(actionProvider.getExit());
			mainToolBar.add(actionProvider.getViewAbout());
			mainToolBar.setOrientation(SwingConstants.VERTICAL);
		}
		return mainToolBar;
	}

	/**
	 * Restituisce la tabbed pane che contiene le schede aperte dell'applicazione.
	 * 
	 * @return la tabbed pane che contiene le schede aperte dell'applicazione
	 */
	public JTabbedPane getMainTabbedPane()
	{
		if(mainTabbedPane == null)
			mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		return mainTabbedPane;
	}

	/**
	 * Restituisce l'etichetta di stato.
	 * 
	 * @return l'etichetta di stato
	 */
	public JLabel getStatusLabel()
	{
		return statusLabel;
	}
}
