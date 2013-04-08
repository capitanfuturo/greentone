package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteOperationAction;
import it.greentone.gui.action.SaveOperationAction;
import it.greentone.gui.action.ViewReportsAction;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;
import it.greentone.report.OperationsReportsCategory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Currency;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
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
public class OperationsPanel extends ContextualPanel<Operation> {
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
	@Inject
	private ViewReportsAction viewReportsAction;
	@Inject
	private OperationsReportsCategory operationsReportsCategory;

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
	private JButton calcButton;

	/**
	 * Pannello di gestione delle operazioni degli incarichi dello studio
	 * professionale.
	 */
	public OperationsPanel() {
		super();

		tableProperties = new String[] { "description", "job", "operationType",
				"isVacazione", "isProfessionalVacazione", "operationDate",
				"amount", "numVacazioni" };
		tableColumnsNames = new String[] {
				getResourceMap().getString(
						LOCALIZATION_PREFIX + "Table.description"),
				getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
				getResourceMap().getString(LOCALIZATION_PREFIX + "Table.type"),
				getResourceMap().getString(
						LOCALIZATION_PREFIX + "Table.isVacazione"),
				getResourceMap().getString(
						LOCALIZATION_PREFIX + "Table.isProfessionalVacazione"),
				getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
				getResourceMap()
						.getString(LOCALIZATION_PREFIX + "Table.amount"),
				getResourceMap().getString(
						LOCALIZATION_PREFIX + "Table.numVacazioni") };
		tableWritables = new boolean[] { false, false, false, false, false,
				false, false, false };
	}

	@Override
	protected JPanel createHeaderPanel() {
		JLabel titleLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "title"));
		titleLabel.setFont(FontProvider.TITLE_SMALL);
		titleLabel.setIcon(getResourceMap().getIcon(
				LOCALIZATION_PREFIX + "titleIcon"));
		JLabel descriptionLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "description"));
		JLabel jobLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "job"));
		JLabel typeLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "type"));
		vacazioneLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "isVacazione"));
		professionalVacazioneLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "isProfessionalVacazione"));
		JLabel dateLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "date"));
		numVacazioniLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "vacazioni"));
		JLabel amountLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "amount"));
		JLabel requiredLabel = new JLabel(getResourceMap().getString(
				LOCALIZATION_PREFIX + "requiredField"));

		JPanel headerPanel = new JPanel(new MigLayout("",
				"[][10%][][10%][][10%]"));

		headerPanel.add(titleLabel, "wrap");

		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "span 3, growx, wrap");

		headerPanel.add(typeLabel, "gap para");
		headerPanel.add(getTypeComboBox(), "growx, wrap");

		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "growx");
		headerPanel.add(vacazioneLabel, "gap para");
		headerPanel.add(getVacazioneCheckBox(), "growx");
		headerPanel.add(professionalVacazioneLabel, "gap para");
		headerPanel.add(getProfessionalVacazioneCheckBox(), "growx, wrap");

		headerPanel.add(dateLabel, "gap para");
		headerPanel.add(getOperationDate(), "growx");
		headerPanel.add(numVacazioniLabel, "gap para");
		headerPanel.add(getNumVacazioniTextField(), "growx");
		headerPanel.add(amountLabel, "gap para");
		headerPanel.add(getAmountTextField(), "growx");
		headerPanel.add(getCalcButton(), "growx, wrap");
		headerPanel.add(requiredLabel);

		return headerPanel;
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

	/**
	 * Restituisce il pulsante di visualizzazione della calcolatrice.
	 * 
	 * @return il pulsante di visualizzazione della calcolatrice
	 */
	public JButton getCalcButton() {
		if (calcButton == null) {
			calcButton = new JButton();
		}
		return calcButton;
	}

	/**
	 * Restituisce il campo descrizione.
	 * 
	 * @return il campo descrizione
	 */
	public JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			descriptionTextField = new JTextField();
			registerComponent(descriptionTextField);
			descriptionTextField.getDocument().addDocumentListener(
					new DocumentListener() {

						@Override
						public void removeUpdate(DocumentEvent e) {
							toogleAction();
						}

						@Override
						public void insertUpdate(DocumentEvent e) {
							toogleAction();
						}

						@Override
						public void changedUpdate(DocumentEvent e) {
							toogleAction();
						}

						private void toogleAction() {
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
	public JComboBox getJobComboBox() {
		if (jobComboBox == null) {
			jobComboBox = new JComboBox();
			registerComponent(jobComboBox);
			jobComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
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
	public JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			registerComponent(typeComboBox);
			OperationType[] typeArray = OperationType.values();
			for (int i = 0; i < typeArray.length; i++) {
				typeComboBox.insertItemAt(typeArray[i].getLocalizedName(), i);
			}
			typeComboBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (typeComboBox.getSelectedIndex() > -1) {
						/*
						 * Issue 57: mostrare i seguenti campi solo se il tipo
						 * di operazione è lavoro: On. a vacazione, Vacazione
						 * aiutante, Vacazioni.
						 */
						boolean isTask = OperationType.values()[typeComboBox
								.getSelectedIndex()] == OperationType.TASK;
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
						getCalcButton().setEnabled(true);
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
	public JCheckBox getVacazioneCheckBox() {
		if (vacazioneCheckBox == null) {
			vacazioneCheckBox = new JCheckBox();
			registerComponent(vacazioneCheckBox);
			/*
			 * Issue 33: alla selezione del flag On. a vacazione, la voce
			 * Importo deve modificarsi in Vacazioni. Il campo Vacazioni, con
			 * flag On. a vacazione, deve contenere numeri senza decimali
			 */
			vacazioneCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boolean isSelected = vacazioneCheckBox.isSelected();
					getAmountTextField().setEnabled(!isSelected);
					getCalcButton().setEnabled(!isSelected);
					getNumVacazioniTextField().setEnabled(isSelected);
					getProfessionalVacazioneCheckBox().setEnabled(isSelected);
					if (!isSelected) {
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
	public JCheckBox getProfessionalVacazioneCheckBox() {
		if (professionalVacazioneCheckBox == null) {
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
	public JXDatePicker getOperationDate() {
		if (operationDate == null) {
			operationDate = GreenToneUtilities.createJXDataPicker();
			registerComponent(operationDate);
		}
		return operationDate;
	}

	/**
	 * Restituisce il campo dell'importo dell'operazione.
	 * 
	 * @return il campo dell'importo dell'operazione
	 */
	public JFormattedTextField getAmountTextField() {
		if (amountTextField == null) {
			DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat
					.getInstance();
			decimalFormat.setCurrency(Currency.getInstance("EUR"));
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setGroupingUsed(false);
			amountTextField = new JFormattedTextField(decimalFormat);
			amountTextField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					amountTextField.setText(amountTextField.getText().replace(
							'.', ','));
				}
			});

			registerComponent(amountTextField);
		}
		return amountTextField;
	}

	/**
	 * Restituisce il campo delle vacazioni per un'operazione.
	 * 
	 * @return il campo delle vacazioni per un'operazione
	 */
	public JFormattedTextField getNumVacazioniTextField() {
		if (numVacazioniTextField == null) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(0);
			numVacazioniTextField = new JFormattedTextField(numberFormat);
			registerComponent(numVacazioniTextField);
		}
		return numVacazioniTextField;
	}

	/**
	 * Issue 32: Controlla che sia possibile abilitare l'azione di salvataggio
	 * di un'operazione:
	 * <ul>
	 * <li>Deve essere assegnato un protocollo</li>
	 * <li>Deve essere assegnato un incarico</li>
	 * <li>Deve essere assegnato un tipo di operazione</li>
	 * </ul>
	 */
	private void toggleSaveAction() {
		saveOperationAction.setSaveOperationActionEnabled(GreenToneUtilities
				.getText(getDescriptionTextField()) != null
				&& getJobComboBox().getSelectedItem() != null
				&& getTypeComboBox().getSelectedItem() != null);
	}

	@Override
	public Operation getItemFromTableRow(int rowIndex) {
		return operationService.getAllOperations().get(rowIndex);
	}

	@Override
	public void initializeToolBar() {
		getContextualToolBar().add(actionProvider.getAddOperation());
		getContextualToolBar().add(actionProvider.getSaveOperation());
		getContextualToolBar().add(actionProvider.getDeleteOperation());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getViewReports());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getViewHelp());
	}

	@Override
	public void initializeForInsertion() {
		super.initializeForInsertion();
		/* disabilito il campo delle vacazioni */
		getProfessionalVacazioneCheckBox().setEnabled(false);
		getNumVacazioniTextField().setEnabled(false);
		/* abilito l'importo */
		getAmountTextField().setEnabled(true);
		getCalcButton().setEnabled(true);
		/* Imposto di default la data odierna */
		getOperationDate().setDate(new DateTime().toDate());
		/* Imposto il tipo a lavoro */
		getTypeComboBox()
				.setSelectedItem(OperationType.TASK.getLocalizedName());
	}

	@Override
	public void initializeForEditing() {
		super.initializeForEditing();
		Operation selectedOperation = getSelectedItem();
		/* aggiorno il pannello */
		getDescriptionTextField().setText(selectedOperation.getDescription());
		Job job = selectedOperation.getJob();
		getJobComboBox().getModel().setSelectedItem(job);
		getTypeComboBox()
				.setSelectedItem(
						selectedOperation.getOperationType() != null ? selectedOperation
								.getOperationType().getLocalizedName() : null);
		getVacazioneCheckBox().setSelected(selectedOperation.getIsVacazione());
		getProfessionalVacazioneCheckBox().setSelected(
				selectedOperation.getIsProfessionalVacazione());
		getOperationDate()
				.setDate(
						selectedOperation.getOperationDate() != null ? selectedOperation
								.getOperationDate().toDate() : null);
		getAmountTextField().setValue(selectedOperation.getAmount());
		getNumVacazioniTextField()
				.setValue(selectedOperation.getNumVacazioni());
		/*
		 * abilito i campi di importo e vacazione a seconda che il flag sia
		 * abilitato
		 */
		getAmountTextField().setEnabled(!selectedOperation.getIsVacazione());
		getCalcButton().setEnabled(!selectedOperation.getIsVacazione());
		getNumVacazioniTextField().setEnabled(
				selectedOperation.getIsVacazione());
		getProfessionalVacazioneCheckBox().setEnabled(
				selectedOperation.getIsVacazione());
	}

	@Override
	public void populateModel() {
		viewReportsAction.setup(operationsReportsCategory);

		getCalcButton().setAction(actionProvider.getViewCalc());
		getCalcButton().setEnabled(false);

		/* aggiorno la lista degli incarichi */
		SortedList<Job> jobsList = new SortedList<Job>(jobService.getAllJobs(),
				new Comparator<Job>() {

					@Override
					public int compare(Job o1, Job o2) {
						return o2.getProtocol().compareToIgnoreCase(
								o1.getProtocol());
					}
				});
		getJobComboBox().setModel(new DefaultEventComboBoxModel<Job>(jobsList));

		tableModel = new EventJXTableModel<Operation>(
				operationService.getAllOperations(),
				new BeanTableFormat<Operation>(Operation.class,
						tableProperties, tableColumnsNames, tableWritables));
		getContentTable().setModel(tableModel);
		getContentTable().setSortOrder(1, SortOrder.DESCENDING);
	}

	@Override
	public void tableSelectionHook() {
		super.tableSelectionHook();
		deleteOperationAction.setDeleteOperationActionEnabled(true);
	}

	@Override
	public void tableSelectionLostHook() {
		super.tableSelectionLostHook();
		deleteOperationAction.setDeleteOperationActionEnabled(false);
	};
}
