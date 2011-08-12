package it.greentone.gui;

import it.greentone.gui.action.ActionProvider;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobCategory;
import it.greentone.persistence.JobCategoryService;
import it.greentone.persistence.JobService;
import it.greentone.persistence.JobStatus;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.util.Calendar;

import javax.inject.Inject;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import ca.odell.glazedlists.swing.EventJXTableModel;

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
 * Pannello di gestione degli incarichi dello studio professionale.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class JobsPanel extends ContextualPanel
{
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private JobService jobService;
	@Inject
	private PersonService personService;
	@Inject
	private JobCategoryService jobCategoryService;

	private static final String LOCALIZATION_PREFIX = "viewJobs.Panel.";
	private final String panelTitle;
	private boolean isNewJob;
	private EventList<Job> jobEventList;
	private EventJXTableModel<Job> tableModel;
	private EventList<JobCategory> jobCategoriesEventList;

	private JTextField protocolTextField;
	private JXDatePicker dueDatePicker;
	private JXDatePicker startDatePicker;
	private JXDatePicker finishDatePicker;
	private JComboBox categoryComboBox;
	private JComboBox statusComboBox;
	private JTextField descriptionTextField;
	private JComboBox customerComboBox;
	private JComboBox managerComboBox;
	private JTextArea notesTextArea;

	/**
	 * Pannello di gestione degli incarichi dello studio professionale.
	 */
	public JobsPanel()
	{
		super();
		panelTitle = getResourceMap().getString(LOCALIZATION_PREFIX + "title");
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JLabel protocolLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
		JLabel dueDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "dueDate"));
		JLabel startDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "startDate"));
		JLabel finishDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "finishDate"));
		JLabel categoryLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "category"));
		JLabel statusLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "status"));
		JLabel descriptionLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "description"));
		JLabel managerLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "manager"));
		JLabel customerLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "customer"));
		JLabel notesLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));

		JPanel headerPanel = new JPanel(new MigLayout());

		headerPanel.add(protocolLabel, "gap para");
		headerPanel.add(getProtocolTextField(), "growx, wrap");
		headerPanel.add(customerLabel, "gap para");
		headerPanel.add(getCustomerComboBox(), "wrap");
		headerPanel.add(managerLabel, "gap para");
		headerPanel.add(getManagerComboBox(), "wrap");
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "wrap");
		headerPanel.add(dueDateLabel, "gap para");
		headerPanel.add(getDueDatePicker());
		headerPanel.add(startDateLabel, "gap para");
		headerPanel.add(getStartDatePicker());
		headerPanel.add(finishDateLabel, "gap para");
		headerPanel.add(getFinishDatePicker(), "wrap");
		headerPanel.add(categoryLabel, "gap para");
		headerPanel.add(getCategoryComboBox());
		headerPanel.add(statusLabel, "gap para");
		headerPanel.add(getStatusComboBox(), "wrap");
		headerPanel.add(notesLabel, "gap para");
		headerPanel.add(getNotesTextArea(), "span, growx");

		return headerPanel;
	}

	@Override
	public void setup()
	{
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddJob());
		getContextualToolBar().add(actionProvider.getSaveJob());
		getContextualToolBar().add(actionProvider.getDeleteJob());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getEditJobStakeholder());
		getContextualToolBar().add(actionProvider.getEditJobCategory());

		/* imposto la modalità di aggiunta incarico */
		setNewJob(true);

		/* aggiorno la tabella degli incarichi */
		jobEventList = new BasicEventList<Job>();
		jobEventList.addAll(jobService.getAllJobs());
		String[] properties =
		  new String[] {"protocol", "customer", "manager", "description",
		    "dueDate", "startDate", "finishDate", "category", "status", "notes"};
		String[] columnsName =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.customer"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.manager"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.dueDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.startDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.finishDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.category"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.status"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.notes")};
		boolean[] writable =
		  new boolean[] {false, false, false, false, false, false, false, false,
		    false, false};

		tableModel =
		  new EventJXTableModel<Job>(jobEventList, properties, columnsName,
		    writable);
		getContentTable().setModel(tableModel);

		/* carico responsabili e committenti */
		EventList<Person> allPersonsEventList = new BasicEventList<Person>();
		allPersonsEventList.addAll(personService.getAllPersons());

		getCustomerComboBox().setModel(
		  new EventComboBoxModel<Person>(allPersonsEventList));
		getManagerComboBox().setModel(
		  new EventComboBoxModel<Person>(allPersonsEventList));
		
		/* carico le categorie */
		jobCategoriesEventList = new BasicEventList<JobCategory>();
		refreshJobCategories();
		ComboBoxModel model =
		  new EventComboBoxModel<JobCategory>(jobCategoriesEventList);
		categoryComboBox.setModel(model);
	}

	/**
	 * Aggiorna il modello delle categorie degli incarichi.
	 */
	public void refreshJobCategories()
	{
		jobCategoriesEventList.clear();
		jobCategoriesEventList.addAll(jobCategoryService.getAllJobCategories());
	}

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

	public JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
			protocolTextField = new JTextField(25);
		return protocolTextField;
	}

	public JXDatePicker getDueDatePicker()
	{
		if(dueDatePicker == null)
			dueDatePicker = new JXDatePicker(Calendar.getInstance().getTime());
		return dueDatePicker;
	}

	public JXDatePicker getStartDatePicker()
	{
		if(startDatePicker == null)
			startDatePicker = new JXDatePicker(Calendar.getInstance().getTime());
		return startDatePicker;
	}

	public JXDatePicker getFinishDatePicker()
	{
		if(finishDatePicker == null)
			finishDatePicker = new JXDatePicker(Calendar.getInstance().getTime());
		return finishDatePicker;
	}

	public JComboBox getCategoryComboBox()
	{
		if(categoryComboBox == null)
			categoryComboBox = new JComboBox();
		return categoryComboBox;
	}

	public JComboBox getStatusComboBox()
	{
		if(statusComboBox == null)
		{
			statusComboBox = new JComboBox();
			JobStatus[] statusArray = JobStatus.values();
			for(int i = 0; i < statusArray.length; i++)
			{
				statusComboBox.insertItemAt(statusArray[i].getLocalizedName(), i);
			}
		}
		return statusComboBox;
	}

	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
			descriptionTextField = new JTextField(30);
		return descriptionTextField;
	}

	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
			notesTextArea = new JTextArea(5, 50);
		return notesTextArea;
	}

	public JComboBox getCustomerComboBox()
	{
		if(customerComboBox == null)
			customerComboBox = new JComboBox();
		return customerComboBox;
	}

	public JComboBox getManagerComboBox()
	{
		if(managerComboBox == null)
			managerComboBox = new JComboBox();
		return managerComboBox;
	}

	public boolean isNewJob()
	{
		return isNewJob;
	}

	public void setNewJob(boolean isNewJob)
	{
		this.isNewJob = isNewJob;
	}
}
