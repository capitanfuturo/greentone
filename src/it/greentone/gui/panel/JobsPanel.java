package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteJobAction;
import it.greentone.gui.action.SaveJobAction;
import it.greentone.gui.action.ViewJobAction;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobCategory;
import it.greentone.persistence.JobCategoryService;
import it.greentone.persistence.JobService;
import it.greentone.persistence.JobStatus;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import javax.inject.Inject;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
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
public class JobsPanel extends ContextualPanel<Job>
{
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private JobService jobService;
	@Inject
	private JobCategoryService jobCategoryService;
	@Inject
	private PersonService personService;
	@Inject
	private SaveJobAction saveJobAction;
	@Inject
	private DeleteJobAction deleteJobAction;
	@Inject
	private ViewJobAction viewJobAction;

	private static final String LOCALIZATION_PREFIX = "viewJobs.Panel.";
	private final String panelBundle;
	private EventJXTableModel<Job> tableModel;

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
	private JComboBox cityField;
	private EventList<String> cities;
	private final String[] properties;
	private final String[] columnsNames;
	private final boolean[] writables;

	/**
	 * Pannello di gestione degli incarichi dello studio professionale.
	 */
	public JobsPanel()
	{
		super();
		panelBundle = "viewJobs";

		properties =
		  new String[] {"protocol", "customer", "manager", "city", "description",
		    "dueDate", "startDate", "finishDate", "category", "status", "notes"};
		columnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.customer"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.manager"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.city"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.dueDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.startDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.finishDate"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.category"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.status"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.notes")};
		writables =
		  new boolean[] {false, false, false, false, false, false, false, false,
		    false, false, false};
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
		JLabel requiredLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "requiredField"));
		JLabel cityLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "city"));

		JPanel headerPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%]"));

		headerPanel.add(protocolLabel, "gap para");
		headerPanel.add(getProtocolTextField(), "growx, wrap");

		headerPanel.add(customerLabel, "gap para");
		headerPanel.add(getCustomerComboBox(), "growx");
		headerPanel.add(managerLabel, "gap para");
		headerPanel.add(getManagerComboBox(), "growx, wrap");

		headerPanel.add(cityLabel, "gap para");
		headerPanel.add(getCityField(), "growx,wrap");

		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "span, growx, wrap");

		headerPanel.add(dueDateLabel, "gap para");
		headerPanel.add(getDueDatePicker(), "growx");
		headerPanel.add(startDateLabel, "gap para");
		headerPanel.add(getStartDatePicker(), "growx");
		headerPanel.add(finishDateLabel, "gap para");
		headerPanel.add(getFinishDatePicker(), "growx, wrap");

		headerPanel.add(categoryLabel, "gap para");
		headerPanel.add(getCategoryComboBox(), "growx");
		headerPanel.add(statusLabel, "gap para");
		headerPanel.add(getStatusComboBox(), "growx, wrap");

		headerPanel.add(notesLabel, "gap para");
		headerPanel.add(new JScrollPane(getNotesTextArea()), "span, growx, wrap");

		headerPanel.add(requiredLabel);

		return headerPanel;
	}

	@Override
	protected JXTable createContentTable()
	{
		final JXTable table = super.createContentTable();
		table.getSelectionModel().addListSelectionListener(
		  new ListSelectionListener()
			  {

				  @Override
				  public void valueChanged(ListSelectionEvent e)
				  {
					  if(!e.getValueIsAdjusting())
					  {
						  int selectedRow = getContentTable().getSelectedRow();
						  if(selectedRow > -1)
						  {
							  setStatus(EStatus.EDIT);
							  int rowIndexToModel = table.convertRowIndexToModel(selectedRow);
							  Job selectedJob = jobService.getAllJobs().get(rowIndexToModel);
							  setSelectedItem(selectedJob);
							  /* aggiorno il pannello */
							  getProtocolTextField().setText(selectedJob.getProtocol());
							  getDueDatePicker().setDate(
							    selectedJob.getDueDate() != null? selectedJob.getDueDate()
							      .toDate(): null);
							  getStartDatePicker().setDate(
							    selectedJob.getStartDate() != null? selectedJob
							      .getStartDate().toDate(): null);
							  getFinishDatePicker().setDate(
							    selectedJob.getFinishDate() != null? selectedJob
							      .getFinishDate().toDate(): null);
							  getCategoryComboBox().getModel().setSelectedItem(
							    selectedJob.getCategory());
							  getStatusComboBox().setSelectedItem(
							    selectedJob.getStatus() != null? selectedJob.getStatus()
							      .getLocalizedName(): null);
							  getDescriptionTextField().setText(selectedJob.getDescription());
							  Person customer = selectedJob.getCustomer();
							  getCustomerComboBox().getModel().setSelectedItem(customer);
							  Person manager = selectedJob.getManager();
							  getManagerComboBox().getModel().setSelectedItem(manager);
							  getNotesTextArea().setText(selectedJob.getNotes());
							  getCityField().setSelectedItem(selectedJob.getCity());
							  /* abilito le azioni legate alla selezione */
							  deleteJobAction.setDeleteJobActionEnabled(true);
							  viewJobAction.setJob(selectedJob);
							  viewJobAction.setViewJobActionEnabled(true);
						  }
						  else
						  {
							  /* disabilito le azioni legate alla selezione */
							  deleteJobAction.setDeleteJobActionEnabled(false);
							  viewJobAction.setJob(null);
							  viewJobAction.setViewJobActionEnabled(false);
						  }
					  }
				  }
			  });
		return table;
	}

	@Override
	public void setup()
	{
		super.setup();
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().add(actionProvider.getAddJob());
		getContextualToolBar().add(actionProvider.getSaveJob());
		getContextualToolBar().add(actionProvider.getDeleteJob());
		getContextualToolBar().add(actionProvider.getViewJob());
		getContextualToolBar().addSeparator();
		// TODO
		// getContextualToolBar().add(actionProvider.getEditJobStakeholder());
		getContextualToolBar().add(actionProvider.getEditJobCategory());

		tableModel =
		  new EventJXTableModel<Job>(
		    jobService.getAllJobs(),
		    new BeanTableFormat<Job>(Job.class, properties, columnsNames, writables));
		getContentTable().setModel(tableModel);
		getContentTable().setSortOrder(0, SortOrder.DESCENDING);

		/* carico responsabili e committenti */
		getCustomerComboBox().setModel(
		  new EventComboBoxModel<Person>(personService.getAllPersons()));
		getManagerComboBox().setModel(
		  new EventComboBoxModel<Person>(personService.getAllPersons()));

		/* carico le categorie */
		ComboBoxModel model =
		  new EventComboBoxModel<JobCategory>(
		    jobCategoryService.getAllJobCategories());
		categoryComboBox.setModel(model);
	}

	/**
	 * Restituisce la lista dei comuni di riferimento.
	 * 
	 * @return la lista dei comuni di riferimento
	 */
	public EventList<String> getCities()
	{
		if(cities == null)
		{
			cities = new BasicEventList<String>();
		}
		return cities;
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}

	/**
	 * Restituisce il campo protocollo.
	 * 
	 * @return il campo protocollo
	 */
	public JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
		{
			protocolTextField = new JTextField();
			registerComponent(protocolTextField);
			protocolTextField.setEnabled(false);

			protocolTextField.getDocument().addDocumentListener(
			  new DocumentListener()
				  {

					  @Override
					  public void removeUpdate(DocumentEvent e)
					  {
						  toogleAction();
					  }

					  @Override
					  public void insertUpdate(DocumentEvent e)
					  {
						  toogleAction();
					  }

					  @Override
					  public void changedUpdate(DocumentEvent e)
					  {
						  toogleAction();
					  }

					  private void toogleAction()
					  {
						  saveJobAction.setSaveJobActionEnabled(GreenToneUtilities
						    .getText(protocolTextField) != null);
					  }
				  });
		}
		return protocolTextField;
	}

	/**
	 * Restituisce il campo scadenza.
	 * 
	 * @return il campo scadenza
	 */
	public JXDatePicker getDueDatePicker()
	{
		if(dueDatePicker == null)
		{
			dueDatePicker = new JXDatePicker();
			registerComponent(dueDatePicker);
		}
		return dueDatePicker;
	}

	/**
	 * Restituisce il campo data di inizio.
	 * 
	 * @return il campo data di inizio
	 */
	public JXDatePicker getStartDatePicker()
	{
		if(startDatePicker == null)
		{
			startDatePicker = new JXDatePicker();
			registerComponent(startDatePicker);
		}
		return startDatePicker;
	}

	/**
	 * Restituisce il campo data di fine.
	 * 
	 * @return il campo data di fine
	 */
	public JXDatePicker getFinishDatePicker()
	{
		if(finishDatePicker == null)
		{
			finishDatePicker = new JXDatePicker();
			registerComponent(finishDatePicker);
		}
		return finishDatePicker;
	}

	/**
	 * Restituisce il campo categoria.
	 * 
	 * @return il campo categoria
	 */
	public JComboBox getCategoryComboBox()
	{
		if(categoryComboBox == null)
		{
			categoryComboBox = new JComboBox();
			registerComponent(categoryComboBox);
		}
		return categoryComboBox;
	}

	/**
	 * Restituisce il campo stato.
	 * 
	 * @return il campo stato
	 */
	public JComboBox getStatusComboBox()
	{
		if(statusComboBox == null)
		{
			statusComboBox = new JComboBox();
			registerComponent(statusComboBox);
			JobStatus[] statusArray = JobStatus.values();
			for(int i = 0; i < statusArray.length; i++)
			{
				statusComboBox.insertItemAt(statusArray[i].getLocalizedName(), i);
			}
		}
		return statusComboBox;
	}

	/**
	 * Restituisce il campo descrizione.
	 * 
	 * @return il campo descrizione
	 */
	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
		{
			descriptionTextField = new JTextField();
			registerComponent(descriptionTextField);
		}
		return descriptionTextField;
	}

	/**
	 * Restituisce il campo note.
	 * 
	 * @return il campo note
	 */
	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
		{
			notesTextArea = new JTextArea(5, 50);
			registerComponent(notesTextArea);
		}
		return notesTextArea;
	}

	/**
	 * Restituisce il campo committente.
	 * 
	 * @return il campo committente
	 */
	public JComboBox getCustomerComboBox()
	{
		if(customerComboBox == null)
		{
			customerComboBox = new JComboBox();
			registerComponent(customerComboBox);
		}
		return customerComboBox;
	}

	/**
	 * Restituisce il campo responsabile.
	 * 
	 * @return il campo responsabile
	 */
	public JComboBox getManagerComboBox()
	{
		if(managerComboBox == null)
		{
			managerComboBox = new JComboBox();
			registerComponent(managerComboBox);
		}
		return managerComboBox;
	}

	/**
	 * Restituisce il campo comune di riferimento.
	 * 
	 * @return il campo comune di riferimento
	 */
	public JComboBox getCityField()
	{
		if(cityField == null)
		{
			cityField = new JComboBox();
			registerComponent(cityField);
			AutoCompleteSupport.install(cityField, getCities());
		}
		return cityField;
	}

	@Override
	public void clearForm()
	{
		super.clearForm();
		/* Issue 74: la data di inizio viene prepopolata con la data corrente */
		getStartDatePicker().setDate(new DateTime().toDate());
		/* Issue 89: autocompletion */
		getCities().clear();
		getCities().addAll(jobService.getAllCities());
		/* Issue 100: lo stato iniziale di un incarico è "In pianificazione" */
		getStatusComboBox().setSelectedItem(JobStatus.PLANNING.getLocalizedName());
	}
}