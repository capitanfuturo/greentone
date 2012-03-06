package it.greentone.gui.panel;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.AbstractPanel;
import it.greentone.gui.ButtonTabComponent;
import it.greentone.gui.MainPanel;
import it.greentone.persistence.Job;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

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
 * Pannello di dettaglio di un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public class JobDetailsPanel extends JPanel
{
	private final MainPanel mainPanel;
	private final ResourceMap resourceMap;
	private final String applicationName;
	private static final String PANEL_TITLE_SUFFIX = ".Panel.title";
	private static final String ACTION_SMALL_ICON_SUFFIX = ".Action.smallIcon";
	private static final String ACTION_TOOLTIP_SUFFIX =
	  ".Action.shortDescription";

	/**
	 * Pannello di dettaglio di un incarico.
	 * 
	 * @param job
	 *          incarico di cui mostrare i dettagli
	 * @param jobPanel
	 * @param mainPanel
	 * @param resourceMap
	 */
	public JobDetailsPanel(final Job job, final JobPanel jobPanel,
	  MainPanel mainPanel, ResourceMap resourceMap)
	{
		this.mainPanel = mainPanel;
		this.resourceMap = resourceMap;
		this.applicationName = resourceMap.getString("Application.name");

		JButton viewDetailsButton = new JButton(new AbstractAction()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					jobPanel.setJob(job);
					jobPanel.setup();
					addTab(jobPanel);
				}
			});
		viewDetailsButton.setIcon(resourceMap.getIcon(jobPanel.getBundleName()
		  + ACTION_SMALL_ICON_SUFFIX));
		viewDetailsButton.setToolTipText(resourceMap
		  .getString("JobDetailsPanel.jobDetails"));

		JLabel descriptionFieldLabel = new JLabel(job.getDescription());
		JLabel dueDateFieldLabel =
		  new JLabel(GreenToneUtilities.formatDateTime(job.getDueDate()));
		JLabel customerFieldLabel =
		  new JLabel(job.getCustomer() != null? job.getCustomer().toString(): null);

		setBorder(BorderFactory.createTitledBorder(job.getProtocol()));
		setLayout(new MigLayout());
		add(viewDetailsButton);
		add(descriptionFieldLabel, "wrap");
		add(new JLabel(" "));
		add(dueDateFieldLabel);
		add(customerFieldLabel);
	}

	protected void addTab(AbstractPanel panel)
	{
		/* controllo che il tab non sia già presente */
		JTabbedPane tabbedPane = mainPanel.getMainTabbedPane();
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
			tabbedPane
			  .insertTab(
			    title,
			    resourceMap.getIcon(panel.getBundleName() + ACTION_SMALL_ICON_SUFFIX),
			    panel,
			    resourceMap.getString(panel.getBundleName() + ACTION_TOOLTIP_SUFFIX),
			    0);
			tabbedPane.setTabComponentAt(0, new ButtonTabComponent(tabbedPane));
		}
		tabbedPane.setSelectedComponent(panel);
	}
}
