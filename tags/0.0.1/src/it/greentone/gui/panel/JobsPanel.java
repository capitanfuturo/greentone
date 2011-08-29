package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteJobAction;
import it.greentone.gui.action.SaveJobAction;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.impl.beans.BeanTableFormat;
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

	private static final String LOCALIZATION_PREFIX = "viewJobs.Panel.";
	private final String panelTitle;
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
	protected JXTable createContentTable()
	{
		JXTable table = super.createContentTable();
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
							  setSelectedItem(jobService.getAllJobs().get(selectedRow));
							  /* aggiorno il pannello */
							  getProtocolTextField().setText(getSelectedItem().getProtocol());
							  getDueDatePicker().setDate(
							    getSelectedItem().getDueDate().toDate());
							  getStartDatePicker().setDate(
							    getSelectedItem().getStartDate().toDate());
							  getFinishDatePicker().setDate(
							    getSelectedItem().getFinishDate().toDate());
							  getCategoryComboBox().setSelectedItem(
							    getSelectedItem().getCategory());
							  getStatusComboBox().setSelectedItem(
							    getSelectedItem().getStatus() != null? getSelectedItem()
							      .getStatus().getLocalizedName(): null);
							  getDescriptionTextField().setText(
							    getSelectedItem().getDescription());
							  getCustomerComboBox().setSelectedItem(
							    getSelectedItem().getCustomer());
							  getManagerComboBox().setSelectedItem(
							    getSelectedItem().getManager());
							  getNotesTextArea().setText(getSelectedItem().getNotes());
							  /* abilito le azioni legate alla selezione */
							  deleteJobAction.setDeleteJobActionEnabled(true);
						  }
						  else
						  {
							  /* disabilito le azioni legate alla selezione */
							  deleteJobAction.setDeleteJobActionEnabled(false);
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
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddJob());
		getContextualToolBar().add(actionProvider.getSaveJob());
		getContextualToolBar().add(actionProvider.getDeleteJob());
		getContextualToolBar().addSeparator();
		// TODO
		// getContextualToolBar().add(actionProvider.getEditJobStakeholder());
		getContextualToolBar().add(actionProvider.getEditJobCategory());

		/* aggiorno la tabella degli incarichi */
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
		  new EventJXTableModel<Job>(jobService.getAllJobs(),
		    new BeanTableFormat<Job>(Job.class, properties, columnsName, writable));
		getContentTable().setModel(tableModel);

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

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

	public JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
		{
			protocolTextField = new JTextField(25);
			registerComponent(protocolTextField);
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

	public JXDatePicker getDueDatePicker()
	{
		if(dueDatePicker == null)
		{
			dueDatePicker = new JXDatePicker();
			registerComponent(dueDatePicker);
		}
		return dueDatePicker;
	}

	public JXDatePicker getStartDatePicker()
	{
		if(startDatePicker == null)
		{
			startDatePicker = new JXDatePicker();
			registerComponent(startDatePicker);
		}
		return startDatePicker;
	}

	public JXDatePicker getFinishDatePicker()
	{
		if(finishDatePicker == null)
		{
			finishDatePicker = new JXDatePicker();
			registerComponent(finishDatePicker);
		}
		return finishDatePicker;
	}

	public JComboBox getCategoryComboBox()
	{
		if(categoryComboBox == null)
		{
			categoryComboBox = new JComboBox();
			registerComponent(categoryComboBox);
		}
		return categoryComboBox;
	}

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

	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
		{
			descriptionTextField = new JTextField(30);
			registerComponent(descriptionTextField);
		}
		return descriptionTextField;
	}

	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
		{
			notesTextArea = new JTextArea(5, 50);
			registerComponent(notesTextArea);
		}
		return notesTextArea;
	}

	public JComboBox getCustomerComboBox()
	{
		if(customerComboBox == null)
		{
			customerComboBox = new JComboBox();
			registerComponent(customerComboBox);
		}
		return customerComboBox;
	}

	public JComboBox getManagerComboBox()
	{
		if(managerComboBox == null)
		{
			managerComboBox = new JComboBox();
			registerComponent(managerComboBox);
		}
		return managerComboBox;
	}
}