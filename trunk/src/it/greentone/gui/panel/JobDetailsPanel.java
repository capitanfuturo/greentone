package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.MainPanel;
import it.greentone.gui.action.ContextualAction;
import it.greentone.persistence.Job;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.ResourceMap;

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
 * Pannello di dettaglio di un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public class JobDetailsPanel extends JPanel
{
	private static final String ACTION_SMALL_ICON_SUFFIX = ".Action.smallIcon";

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
	  final MainPanel mainPanel, ResourceMap resourceMap)
	{
		JButton viewDetailsButton = new JButton(new AbstractAction()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					jobPanel.setJob(job);
					jobPanel.setup();
					ContextualAction.addTab(mainPanel, jobPanel);
				}
			});
		viewDetailsButton.setIcon(resourceMap.getIcon(jobPanel.getBundleName()
		  + ACTION_SMALL_ICON_SUFFIX));
		viewDetailsButton.setToolTipText(resourceMap
		  .getString("JobDetailsPanel.jobDetails"));

		JLabel protocolLabel = new JLabel(job.getProtocol());
		protocolLabel.setFont(FontProvider.TITLE_SMALL);
		JLabel descriptionFieldLabel = new JLabel(job.getDescription());
		descriptionFieldLabel.setFont(FontProvider.PARAGRAPH_BIG);
		JLabel dueDateFieldLabel =
		  new JLabel(GreenToneUtilities.formatDateTime(job.getDueDate()));
		JLabel customerFieldLabel =
		  new JLabel(job.getCustomer() != null? job.getCustomer().toString(): null);

		setLayout(new MigLayout());
		add(viewDetailsButton, "span 1 2");
		add(protocolLabel);
		add(descriptionFieldLabel, "wrap");
		add(new JLabel(resourceMap.getString("JobDetailsPanel.duedate")));
		add(dueDateFieldLabel);
		add(new JLabel(resourceMap.getString("JobDetailsPanel.customer")));
		add(customerFieldLabel, "wrap");
	}
}
