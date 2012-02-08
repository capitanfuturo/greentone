package it.greentone.gui.panel;

import it.greentone.persistence.Job;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.joda.time.DateTime;

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

	/**
	 * Pannello di dettaglio di un incarico.
	 * 
	 * @param job
	 *          incarico di cui mostrare i dettagli
	 */
	public JobDetailsPanel(Job job)
	{
		JLabel protocolFieldLabel = new JLabel(job.getProtocol());
		JLabel descriptionFieldLabel = new JLabel(job.getDescription());

		String formattedDate = "";
		DateTime date = job.getDueDate();
		if(date != null)
		{
			formattedDate =
			  date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/"
			    + date.getYear();
		}
		JLabel dueDateFieldLabel = new JLabel(formattedDate);
		JLabel customerFieldLabel =
		  new JLabel(job.getCustomer() != null? job.getCustomer().toString(): null);

		setLayout(new MigLayout());
		add(protocolFieldLabel);
		add(descriptionFieldLabel);
		add(dueDateFieldLabel);
		add(customerFieldLabel);
	}
}
