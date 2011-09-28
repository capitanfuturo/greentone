package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteOperationAction;
import it.greentone.gui.action.SaveOperationAction;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;
import it.greentone.persistence.OperationTypeService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * Pannello di gestione delle operazioni degli incarichi dello studio
 * professionale.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class OperationsPanel extends ContextualPanel<Operation>
{
	private static final String BUNDLE_NAME = "viewOperations";
	private static final String LOCALIZATION_PREFIX = BUNDLE_NAME + ".Panel.";
	@Inject
	private OperationService operationService;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private JobService jobService;
	@Inject
	OperationTypeService operationTypeService;
	@Inject
	DeleteOperationAction deleteOperationAction;
	@Inject
	SaveOperationAction saveOperationAction;
	private EventJXTableModel<Operation> tableModel;

	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox typeComboBox;
	private JCheckBox vacazioneCheckBox;
	private JCheckBox professionalVacazioneCheckBox;
	private JXDatePicker operationDate;
	private JFormattedTextField amountTextField;
	private String[] tableProperties;
	private String[] tableColumnsNames;
	private boolean[] tableWritables;
	private JLabel amountLabel;

	/**
	 * Pannello di gestione delle operazioni degli incarichi dello studio
	 * professionale.
	 */
	public OperationsPanel()
	{
		super();
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JLabel descriptionLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "description"));
		JLabel jobLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "job"));
		JLabel typeLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "type"));
		JLabel isVacazioneLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "isVacazione"));
		JLabel isProfessionalVacazioneLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "isProfessionalVacazione"));
		JLabel dateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));


		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "growx, wrap");

		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "wrap");

		headerPanel.add(typeLabel, "gap para");
		headerPanel.add(getTypeComboBox());
		headerPanel.add(isVacazioneLabel, "gap para");
		headerPanel.add(getVacazioneCheckBox());
		headerPanel.add(isProfessionalVacazioneLabel, "gap para");
		headerPanel.add(getProfessionalVacazioneCheckBox(), "wrap");

		headerPanel.add(dateLabel, "gap para");
		headerPanel.add(getOperationDate());
		headerPanel.add(getAmountLabel(), "gap para");
		headerPanel.add(getAmountTextField(), "growx");

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
							  Operation selectedOperation =
							    operationService.getAllOperations().get(selectedRow);
							  setSelectedItem(selectedOperation);

							  /* aggiorno il pannello */
							  getDescriptionTextField().setText(
							    selectedOperation.getDescription());
							  Job job = selectedOperation.getJob();
							  getJobComboBox().getModel().setSelectedItem(job);
							  OperationType operationType =
							    selectedOperation.getOperationType();
							  getTypeComboBox().getModel().setSelectedItem(operationType);
							  getVacazioneCheckBox().setSelected(
							    selectedOperation.getIsVacazione());
							  if(selectedOperation.getIsVacazione())
							  {
								  getAmountLabel().setText(
								    getResourceMap().getString(
								      LOCALIZATION_PREFIX + "vacazioni"));
							  }
							  else
							  {
								  getAmountLabel().setText(
								    getResourceMap().getString(LOCALIZATION_PREFIX + "amount"));
							  }
							  getProfessionalVacazioneCheckBox().setSelected(
							    selectedOperation.getIsProfessionalVacazione());
							  getOperationDate().setDate(
							    selectedOperation.getOperationDate() != null
							      ? selectedOperation.getOperationDate().toDate()
							      : null);
							  getAmountTextField().setValue(
							    selectedOperation.getAmount() != null? selectedOperation
							      .getAmount(): null);

							  /* abilito le azioni legate alla selezione */
							  deleteOperationAction.setDeleteOperationActionEnabled(true);
						  }
						  else
						  {
							  /* disabilito le azioni legate alla selezione */
							  deleteOperationAction.setDeleteOperationActionEnabled(false);
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
		getContextualToolBar().add(actionProvider.getAddOperation());
		getContextualToolBar().add(actionProvider.getSaveOperation());
		getContextualToolBar().add(actionProvider.getDeleteOperation());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getEditOperationType());
		getContextualToolBar().add(actionProvider.getEditOperationTypeTypology());

		/* aggiorno la lista degli incarichi */
		getJobComboBox().setModel(
		  new EventComboBoxModel<Job>(jobService.getAllJobs()));

		/* aggiorno la lista dei tipi */
		getTypeComboBox().setModel(
		  new EventComboBoxModel<OperationType>(operationTypeService
		    .getAllOperationTypes()));

		tableProperties =
		  new String[] {"description", "job", "operationType", "isVacazione",
		    "isProfessionalVacazione", "operationDate", "amount"};
		tableColumnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.type"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isVacazione"),
		    getResourceMap().getString(
		      LOCALIZATION_PREFIX + "Table.isProfessionalVacazione"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.amount")};
		tableWritables =
		  new boolean[] {false, false, false, false, false, false, false};

		tableModel =
		  new EventJXTableModel<Operation>(operationService.getAllOperations(),
		    new BeanTableFormat<Operation>(Operation.class, tableProperties,
		      tableColumnsNames, tableWritables));
		getContentTable().setModel(tableModel);
	}

	@Override
	public String getBundleName()
	{
		return BUNDLE_NAME;
	}

	/**
	 * Restituisce l'etichetta del campo importo/vacazione.
	 * 
	 * @return l'etichetta del campo importo/vacazione
	 */
	public JLabel getAmountLabel()
	{
		if(amountLabel == null)
		{
			amountLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "amount"));
		}
		return amountLabel;
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
			descriptionTextField = new JTextField(20);
			registerComponent(descriptionTextField);
			descriptionTextField.getDocument().addDocumentListener(
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
						  toggleSaveAction();
					  }
				  });
		}
		return descriptionTextField;
	}

	/**
	 * Restituisce l'elenco dei tipi degli incarichi disponibili.
	 * 
	 * @return l'elenco dei tipi degli incarichi disponibili
	 */
	public JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
		{
			jobComboBox = new JComboBox();
			registerComponent(jobComboBox);
			jobComboBox.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						toggleSaveAction();
					}
				});
		}
		return jobComboBox;
	}

	/**
	 * Restituisce l'elenco dei tipi di operazione disponibili.
	 * 
	 * @return l'elenco dei tipi di operazione disponibili
	 */
	public JComboBox getTypeComboBox()
	{
		if(typeComboBox == null)
		{
			typeComboBox = new JComboBox();
			registerComponent(typeComboBox);
		}
		return typeComboBox;
	}

	/**
	 * Restituisce il flag che dichiara se l'operazione è a vacazione.
	 * 
	 * @return il flag che dichiara se l'operazione è a vacazione
	 */
	public JCheckBox getVacazioneCheckBox()
	{
		if(vacazioneCheckBox == null)
		{
			vacazioneCheckBox = new JCheckBox();
			registerComponent(vacazioneCheckBox);
			/*
			 * Issue 33: alla selezione del flag On. a vacazione, la voce Importo deve
			 * modificarsi in Vacazioni. Il campo Vacazioni, con flag On. a vacazione,
			 * deve contenere numeri senza decimali
			 */
			vacazioneCheckBox.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(vacazioneCheckBox.isSelected())
						{
							getAmountLabel().setText(
							  getResourceMap().getString(LOCALIZATION_PREFIX + "vacazioni"));
						}
						else
						{
							getAmountLabel().setText(
							  getResourceMap().getString(LOCALIZATION_PREFIX + "amount"));
						}
					}
				});
		}
		return vacazioneCheckBox;
	}

	/**
	 * Restituisce il flag che dichiara se l'operazione è a vacazione
	 * professionale.
	 * 
	 * @return il flag che dichiara se l'operazione è a vacazione professionale
	 */
	public JCheckBox getProfessionalVacazioneCheckBox()
	{
		if(professionalVacazioneCheckBox == null)
		{
			professionalVacazioneCheckBox = new JCheckBox();
			registerComponent(professionalVacazioneCheckBox);
		}
		return professionalVacazioneCheckBox;
	}

	/**
	 * Restituisce il campo della data in cui è stata effettuata l'operazione.
	 * 
	 * @return il campo della data in cui è stata effettuata l'operazione
	 */
	public JXDatePicker getOperationDate()
	{
		if(operationDate == null)
		{
			operationDate = new JXDatePicker();
			registerComponent(operationDate);
		}
		return operationDate;
	}

	/**
	 * Restituisce il campo dell'importo dell'operazione.
	 * 
	 * @return il campo dell'importo dell'operazione
	 */
	public JFormattedTextField getAmountTextField()
	{
		if(amountTextField == null)
		{
			DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
			decimalFormat.setMaximumFractionDigits(2);
			amountTextField = new JFormattedTextField(decimalFormat);
			amountTextField.setColumns(10);
			registerComponent(amountTextField);
		}
		return amountTextField;
	}

	/**
	 * Issue 32: Controlla che sia possibile abilitare l'azione di salvataggio di
	 * un'operazione.
	 */
	private void toggleSaveAction()
	{
		saveOperationAction.setSaveOperationActionEnabled(GreenToneUtilities
		  .getText(getDescriptionTextField()) != null
		  && getJobComboBox().getSelectedItem() != null);
	}
}
