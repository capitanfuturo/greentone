package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.MainPanel;
import it.greentone.gui.action.ContextualAction;
import it.greentone.persistence.Job;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
public class JobDetailsPanel extends JPanel {

	private static final String ACTION_ICON_SUFFIX = ".Action.largeIcon";

	/**
	 * Pannello di dettaglio di un incarico.
	 * 
	 * @param job
	 *            incarico di cui mostrare i dettagli
	 * @param jobPanel
	 * @param mainPanel
	 * @param resourceMap
	 */
	public JobDetailsPanel(final Job job, final JobPanel jobPanel,
			final MainPanel mainPanel, ResourceMap resourceMap) {
		JButton viewDetailsButton = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jobPanel.setJob(job);
				jobPanel.setup();
				ContextualAction.addTab(mainPanel, jobPanel);
			}
		});
		viewDetailsButton.setIcon(resourceMap.getIcon(jobPanel.getBundleName()
				+ ACTION_ICON_SUFFIX));
		viewDetailsButton.setToolTipText(resourceMap
				.getString("JobDetailsPanel.jobDetails"));
		String viewDetailsText = job.getProtocol();
		if (job.getDescription() != null) {
			viewDetailsText = viewDetailsText + " " + job.getDescription();
		}
		viewDetailsButton.setText(viewDetailsText);
		viewDetailsButton.setFont(FontProvider.TITLE_SMALL);
		viewDetailsButton.setHorizontalAlignment(SwingConstants.LEFT);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(viewDetailsButton, BorderLayout.CENTER);

		JPanel southPanel = new JPanel(new BorderLayout());
		String customerString = job.getCustomer() != null ? job.getCustomer()
				.toString() : null;
		JLabel customerLabel = new JLabel(customerString);
		customerLabel.setFont(FontProvider.PARAGRAPH_BIG);
		JPanel leftPanel = new JPanel(new MigLayout());
		leftPanel.add(new JLabel(resourceMap
				.getString("JobDetailsPanel.customer")));
		leftPanel.add(customerLabel);
		southPanel.add(leftPanel, BorderLayout.WEST);

		JPanel rightPanel = new JPanel(new MigLayout());
		if (job.getDueDate() != null) {
			rightPanel.add(new JLabel(resourceMap
					.getString("JobDetailsPanel.duedate")
					+ GreenToneUtilities.formatDateTime(job.getDueDate())));
		} else {
			rightPanel.add(new JLabel());
		}
		southPanel.add(rightPanel, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.CENTER);
	}
}
