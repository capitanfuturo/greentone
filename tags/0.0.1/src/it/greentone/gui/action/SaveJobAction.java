package it.greentone.gui.action;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.JobsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobCategory;
import it.greentone.persistence.JobService;
import it.greentone.persistence.JobStatus;
import it.greentone.persistence.Person;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
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
 * Azione di salvataggio di un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveJobAction extends AbstractBean
{
	@Inject
	private JobsPanel jobsPanel;
	@Inject
	private JobService jobService;
	boolean saveJobActionEnabled = false;

	/**
	 * Azione di salvataggio di un incarico.
	 */
	@Action(enabledProperty = "saveJobActionEnabled")
	public void saveJob()
	{
		/*
		 * se si tratta di una nuova entry creo un nuovo incarico altrimenti
		 * modifico quella selezionato
		 */
		Job job =
		  jobsPanel.getStatus() == EStatus.EDIT
		    ? jobsPanel.getSelectedItem()
		    : new Job();
		/* compilo il bean */
		job
		  .setProtocol(GreenToneUtilities.getText(jobsPanel.getProtocolTextField()));
		job.setCustomer((Person) jobsPanel.getCustomerComboBox().getSelectedItem());
		job.setManager((Person) jobsPanel.getManagerComboBox().getSelectedItem());
		job.setDescription(GreenToneUtilities.getText(jobsPanel
		  .getDescriptionTextField()));
		job.setDueDate(GreenToneUtilities.getDateTime(jobsPanel.getDueDatePicker()));
		job
		  .setStartDate(GreenToneUtilities.getDateTime(jobsPanel.getStartDatePicker()));
		job.setFinishDate(GreenToneUtilities.getDateTime(jobsPanel
		  .getFinishDatePicker()));
		job.setCategory((JobCategory) jobsPanel.getCategoryComboBox()
		  .getSelectedItem());
		if(jobsPanel.getStatusComboBox().getSelectedIndex() > -1)
		{
			job.setStatus(JobStatus.values()[jobsPanel.getStatusComboBox()
			  .getSelectedIndex()]);
		}
		job.setNotes(jobsPanel.getNotesTextArea().getText());
		/* aggiorno la tabella */
		if(jobsPanel.getStatus() == EStatus.NEW)
		{
			jobService.addJob(job);
		}
		else
		{
			jobService.storeJob(job);
		}
		jobsPanel.clearForm();
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isSaveJobActionEnabled()
	{
		return saveJobActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param saveJobActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setSaveJobActionEnabled(boolean saveJobActionEnabled)
	{
		final boolean oldValue = this.saveJobActionEnabled;
		this.saveJobActionEnabled = saveJobActionEnabled;
		firePropertyChange("saveJobActionEnabled", oldValue, saveJobActionEnabled);
	}
}
