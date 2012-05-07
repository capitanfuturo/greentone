package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.ButtonTabComponent;
import it.greentone.gui.MainPanel;
import it.greentone.gui.panel.AbstractPanel;
import it.greentone.gui.panel.HomePanel;
import it.greentone.gui.panel.JobPanel;
import it.greentone.persistence.Job;

import javax.inject.Inject;
import javax.swing.JTabbedPane;

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
 * Mostra un incarico e i suoi dettagli nel pannello principale
 * dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewJobAction extends AbstractBean
{
	@Inject
	private JobPanel jobPanel;
	private Job job;
	boolean viewJobActionEnabled = false;


	private static final String PANEL_TITLE_SUFFIX = ".Panel.title";
	private static final String ACTION_SMALL_ICON_SUFFIX = ".Action.smallIcon";
	private static final String ACTION_TOOLTIP_SUFFIX =
	  ".Action.shortDescription";
	private final MainPanel mainPanel;
	private final ResourceMap resourceMap;
	private final String applicationName;

	/**
	 * Mostra gli incarichi nel pannello principale dell'applicazione.
	 * 
	 * @param mainPanel
	 *          pannello principale
	 */
	@Inject
	public ViewJobAction(MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		this.applicationName = resourceMap.getString("Application.title");
	}

	/**
	 * Imposta l'incarico da visualizzare.
	 * 
	 * @param job
	 *          l'incarico da visualizzare
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}

	/**
	 * Mostra gli incarichi nel pannello principale dell'applicazione.
	 */
	@Action(enabledProperty = "viewJobActionEnabled")
	public void viewJob()
	{
		jobPanel.setJob(job);
		jobPanel.setup();
		addTab(jobPanel);
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isViewJobActionEnabled()
	{
		return viewJobActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param viewJobActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setViewJobActionEnabled(boolean viewJobActionEnabled)
	{
		final boolean oldValue = this.viewJobActionEnabled;
		this.viewJobActionEnabled = viewJobActionEnabled;
		firePropertyChange("viewJobActionEnabled", oldValue, viewJobActionEnabled);
	}

	protected ResourceMap getResourceMap()
	{
		return resourceMap;
	}

	protected MainPanel getMainPanel()
	{
		return mainPanel;
	}

	protected void addTab(AbstractPanel panel)
	{
		/* controllo che il tab non sia già presente */
		JTabbedPane tabbedPane = getMainPanel().getMainTabbedPane();
		boolean tabInserted = false;
		for(int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			java.awt.Component componentAt = tabbedPane.getComponentAt(i);
			if(componentAt.equals(panel))
			{
				tabInserted = true;
			}
		}
		if(!tabInserted)
		{
			String title =
			  resourceMap.getString(panel.getBundleName() + PANEL_TITLE_SUFFIX);
			/* aggiorno il titolo dell'applicazione */
			Application.getInstance(GreenTone.class).getMainFrame()
			  .setTitle(title + " - " + applicationName);
			/* configuro e aggiungo il tab */
			panel.setup();
			/*
			 * Issue 126: la pagina iniziale deve rimanere in prima posizione e non si
			 * può chiudere
			 */
			if(panel instanceof HomePanel)
			{
				tabbedPane
				  .insertTab(title, resourceMap.getIcon(panel.getBundleName()
				    + ACTION_SMALL_ICON_SUFFIX), panel, resourceMap.getString(panel
				    .getBundleName() + ACTION_TOOLTIP_SUFFIX), 0);
			}
			else
			{
				tabbedPane
				  .insertTab(title, resourceMap.getIcon(panel.getBundleName()
				    + ACTION_SMALL_ICON_SUFFIX), panel, resourceMap.getString(panel
				    .getBundleName() + ACTION_TOOLTIP_SUFFIX), 1);
				tabbedPane.setTabComponentAt(1, new ButtonTabComponent(tabbedPane));
			}
		}
		tabbedPane.setSelectedComponent(panel);
	}
}
