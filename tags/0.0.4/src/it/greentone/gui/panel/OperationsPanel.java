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
import org.joda.time.DateTime;
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
	private final String[] tableProperties;
	private final String[] tableColumnsNames;
	private final boolean[] tableWritables;
	private JLabel vacazioneLabel;
	private JLabel professionalVacazioneLabel;
	private JLabel numVacazioniLabel;

	/**
	 * Pannello di gestione delle operazioni degli incarichi dello studio
	 * professionale.
	 */
	public OperationsPanel()
	{
		super();

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
		vacazioneLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "isVacazione"));
		professionalVacazioneLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "isProfessionalVacazione"));
		JLabel dateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));
		numVacazioniLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "vacazioni"));
		JLabel amountLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "amount"));
		JLabel requiredLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "requiredField"));


		JPanel headerPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%]"));
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "span 3, growx, wrap");

		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "growx, wrap");

		headerPanel.add(typeLabel, "gap para");
		headerPanel.add(getTypeComboBox(), "growx");
		headerPanel.add(vacazioneLabel, "gap para");
		headerPanel.add(getVacazioneCheckBox(), "growx");
		headerPanel.add(professionalVacazioneLabel, "gap para");
		headerPanel.add(getProfessionalVacazioneCheckBox(), "growx, wrap");

		headerPanel.add(dateLabel, "gap para");
		headerPanel.add(getOperationDate(), "growx");
		headerPanel.add(numVacazioniLabel, "gap para");
		headerPanel.add(getNumVacazioniTextField(), "growx");
		headerPanel.add(amountLabel, "gap para");
		headerPanel.add(getAmountTextField(), "growx, wrap");
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
							  Operation selectedOperation =
							    operationService.getAllOperations().get(rowIndexToModel);
							  setSelectedItem(selectedOperation);

							  /* aggiorno il pannello */
							  getDescriptionTextField().setText(
							    selectedOperation.getDescription());
							  Job job = selectedOperation.getJob();
							  getJobComboBox().getModel().setSelectedItem(job);
							  getTypeComboBox().setSelectedItem(
							    selectedOperation.getOperationType() != null
							      ? selectedOperation.getOperationType().getLocalizedName()
							      : null);
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
		getContextualToolBar().add(actionProvider.getAddOperation());
		getContextualToolBar().add(actionProvider.getSaveOperation());
		getContextualToolBar().add(actionProvider.getDeleteOperation());

		/* aggiorno la lista degli incarichi */
		getJobComboBox().setModel(
		  new EventComboBoxModel<Job>(jobService.getAllJobs()));

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
			descriptionTextField = new JTextField();
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
			OperationType[] typeArray = OperationType.values();
			for(int i = 0; i < typeArray.length; i++)
			{
				typeComboBox.insertItemAt(typeArray[i].getLocalizedName(), i);
			}
			typeComboBox.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(typeComboBox.getSelectedIndex() > -1)
						{
							/*
							 * Issue 57: mostrare i seguenti campi solo se il tipo di
							 * operazione è lavoro: On. a vacazione, Vacazione aiutante,
							 * Vacazioni.
							 */
							boolean isTask =
							  OperationType.values()[typeComboBox.getSelectedIndex()] == OperationType.TASK;
							getVacazioneCheckBox().setVisible(isTask);
							vacazioneLabel.setVisible(isTask);
							getProfessionalVacazioneCheckBox().setVisible(isTask);
							professionalVacazioneLabel.setVisible(isTask);
							getNumVacazioniTextField().setVisible(isTask);
							numVacazioniLabel.setVisible(isTask);
							getVacazioneCheckBox().setSelected(false);
							getProfessionalVacazioneCheckBox().setSelected(false);
							getNumVacazioniTextField().setEnabled(false);
							getAmountTextField().setEnabled(true);
							/* Issue 60: abilitazione del tasto salva mancante */
							toggleSaveAction();
						}
					}
				});
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
			registerComponent(numVacazioniTextField);
		}
		return numVacazioniTextField;
	}

	/**
	 * Issue 32: Controlla che sia possibile abilitare l'azione di salvataggio di
	 * un'operazione:
	 * <ul>
	 * <li>Deve essere assegnato un protocollo</li>
	 * <li>Deve essere assegnato un incarico</li>
	 * <li>Deve essere assegnato un tipo di operazione</li>
	 * </ul>
	 */
	private void toggleSaveAction()
	{
		saveOperationAction.setSaveOperationActionEnabled(GreenToneUtilities
		  .getText(getDescriptionTextField()) != null
		  && getJobComboBox().getSelectedItem() != null
		  && getTypeComboBox().getSelectedItem() != null);
	}

	@Override
	public void clearForm()
	{
		super.clearForm();
		getNumVacazioniTextField().setEnabled(false);
		getAmountTextField().setEnabled(true);
		/* Imposto di default la data odierna */
		getOperationDate().setDate(new DateTime().toDate());
		/* Imposto il tipo a lavoro */
		getTypeComboBox().setSelectedItem(OperationType.TASK.getLocalizedName());
	}
}