package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.AbstractPanel.EStatus;
import it.greentone.gui.panel.HomePanel;
import it.greentone.gui.panel.JobsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobCategory;
import it.greentone.persistence.JobService;
import it.greentone.persistence.JobStatus;
import it.greentone.persistence.Person;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.joda.time.DateTime;
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
	private HomePanel homePanel;
	@Inject
	private JobService jobService;
	private final ResourceMap resourceMap;
	boolean saveJobActionEnabled = false;

	/**
	 * Azione di salvataggio di un incarico.
	 */
	public SaveJobAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Azione di salvataggio di un incarico.
	 */
	@Action(enabledProperty = "saveJobActionEnabled")
	public void saveJob()
	{
		/*
		 * Issue 63, 64, 65: controllo la validità delle date.
		 */
		DateTime dueDate =
		  GreenToneUtilities.getDateTime(jobsPanel.getDueDatePicker());
		DateTime startDate =
		  GreenToneUtilities.getDateTime(jobsPanel.getStartDatePicker());
		DateTime finishDate =
		  GreenToneUtilities.getDateTime(jobsPanel.getFinishDatePicker());
		/* la scadenza non può essere precedente ad oggi */
		if(dueDate != null)
		{
			if(dueDate.isBeforeNow())
			{
				JOptionPane.showMessageDialog(jobsPanel,
				  resourceMap.getString("saveJob.Action.dueDateBeforeNow"),
				  resourceMap.getString("ErrorMessage.title"),
				  JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		/* la data di inizio non può essere nel futuro */
		if(startDate != null)
		{
			if(startDate.isAfterNow())
			{
				JOptionPane.showMessageDialog(jobsPanel,
				  resourceMap.getString("saveJob.Action.startDateAfterNow"),
				  resourceMap.getString("ErrorMessage.title"),
				  JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		/*
		 * la data di fine non può essere antecedente la data di inizio e non può
		 * essere posizionata nel futuro
		 */
		if(finishDate != null)
		{
			if(startDate != null && finishDate.isBefore(startDate))
			{
				JOptionPane.showMessageDialog(jobsPanel,
				  resourceMap.getString("saveJob.Action.finishDateBeforeStartDate"),
				  resourceMap.getString("ErrorMessage.title"),
				  JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(finishDate.isAfterNow())
			{
				JOptionPane.showMessageDialog(jobsPanel,
				  resourceMap.getString("saveJob.Action.finishDateAfterNow"),
				  resourceMap.getString("ErrorMessage.title"),
				  JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

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
		job.setDueDate(dueDate);
		/*
		 * Issue 26: la data di inizio deve essere sempre impostata, se non presente
		 * viene assegnata automaticamente alla data odierna.
		 */
		job.setStartDate(startDate != null? startDate: new DateTime());
		job.setFinishDate(finishDate);
		job.setCategory((JobCategory) jobsPanel.getCategoryComboBox()
		  .getSelectedItem());
		if(jobsPanel.getStatusComboBox().getSelectedIndex() > -1)
		{
			job.setStatus(JobStatus.values()[jobsPanel.getStatusComboBox()
			  .getSelectedIndex()]);
		}
		job.setNotes(jobsPanel.getNotesTextArea().getText());
		String city =
		  jobsPanel.getCityField().getSelectedItem() != null? jobsPanel
		    .getCityField().getSelectedItem().toString(): null;
		job.setCity(city);
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
		jobsPanel.getContentTable().clearSelection();

		homePanel.setup();
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
