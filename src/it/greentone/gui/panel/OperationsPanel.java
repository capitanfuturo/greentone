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
import java.text.NumberFormat;

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
	private JFormattedTextField numVacazioniTextField;
	private String[] tableProperties;
	private String[] tableColumnsNames;
	private boolean[] tableWritables;

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
		JLabel numVacazioniLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "vacazioni"));
		JLabel amountLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "amount"));


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
		headerPanel.add(numVacazioniLabel, "gap para");
		headerPanel.add(getNumVacazioniTextField());
		headerPanel.add(amountLabel, "gap para");
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
							  getProfessionalVacazioneCheckBox().setSelected(
							    selectedOperation.getIsProfessionalVacazione());
							  getOperationDate().setDate(
							    selectedOperation.getOperationDate() != null
							      ? selectedOperation.getOperationDate().toDate()
							      : null);
							  getAmountTextField().setValue(selectedOperation.getAmount());
							  getNumVacazioniTextField().setValue(
							    selectedOperation.getNumVacazioni());
							  /*
								 * abilito i campi di importo e vacazione a seconda che il flag
								 * sia abilitato
								 */
							  getAmountTextField().setEnabled(
							    !selectedOperation.getIsVacazione());
							  getNumVacazioniTextField().setEnabled(
							    selectedOperation.getIsVacazione());
							  getProfessionalVacazioneCheckBox().setEnabled(
							    selectedOperation.getIsVacazione());

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
		    "isProfessionalVacazione", "operationDate", "amount", "numVacazioni"};
		tableColumnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.type"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isVacazione"),
		    getResourceMap().getString(
		      LOCALIZATION_PREFIX + "Table.isProfessionalVacazione"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.amount"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.numVacazioni")};
		tableWritables =
		  new boolean[] {false, false, false, false, false, false, false, false};

		tableModel =
		  new EventJXTableModel<Operation>(operationService.getAllOperations(),
		    new BeanTableFormat<Operation>(Operation.class, tableProperties,
		      tableColumnsNames, tableWritables));
		getContentTable().setModel(tableModel);

		/* disabilito il campo delle vacazioni */
		getNumVacazioniTextField().setEnabled(false);
		getProfessionalVacazioneCheckBox().setEnabled(false);
		/* abilito quello dell'importo */
		getAmountTextField().setEnabled(true);
	}

	@Override
	public String getBundleName()
	{
		return BUNDLE_NAME;
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
						boolean isSelected = vacazioneCheckBox.isSelected();
						getAmountTextField().setEnabled(!isSelected);
						getNumVacazioniTextField().setEnabled(isSelected);
						getProfessionalVacazioneCheckBox().setEnabled(isSelected);
						if(!isSelected)
						{
							getProfessionalVacazioneCheckBox().setSelected(false);
						}
						/* resetto i campi */
						getAmountTextField().setText(null);
						getNumVacazioniTextField().setText(null);
					}
				});
		}
		return vacazioneCheckBox;
	}

	/**
	 * Restituisce il flag che dichiara se l'operazione è a vacazione di un
	 * aiutante.
	 * 
	 * @return il flag che dichiara se l'operazione è a vacazione di un aiutante
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
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setMaximumFractionDigits(2);
			amountTextField = new JFormattedTextField(decimalFormat);
			amountTextField.setColumns(10);
			registerComponent(amountTextField);
		}
		return amountTextField;
	}

	/**
	 * Restituisce il campo delle vacazioni per un'operazione.
	 * 
	 * @return il campo delle vacazioni per un'operazione
	 */
	public JFormattedTextField getNumVacazioniTextField()
	{
		if(numVacazioniTextField == null)
		{
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(0);
			numVacazioniTextField = new JFormattedTextField(numberFormat);
			numVacazioniTextField.setColumns(4);
			registerComponent(numVacazioniTextField);
		}
		return numVacazioniTextField;
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

	@Override
	public void clearForm()
	{
		super.clearForm();
		getNumVacazioniTextField().setEnabled(false);
		getAmountTextField().setEnabled(true);
	}
}
