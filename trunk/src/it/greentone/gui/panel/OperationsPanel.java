package it.greentone.gui.panel;

import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;
import it.greentone.persistence.OperationTypeService;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
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
	private static final String LOCALIZATION_PREFIX = "viewOperations.Panel.";
	private final String panelTitle;
	@Inject
	private OperationService operationService;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private JobService jobService;
	@Inject
	OperationTypeService operationTypeService;
	private EventJXTableModel<Operation> tableModel;

	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox typeComboBox;
	private JCheckBox vacazioneCheckBox;
	private JCheckBox professionalVacazioneCheckBox;
	private JXDatePicker operationDate;
	private JTextField amountTextField;

	/**
	 * Pannello di gestione delle operazioni degli incarichi dello studio
	 * professionale.
	 */
	public OperationsPanel()
	{
		super();
		panelTitle = getResourceMap().getString(LOCALIZATION_PREFIX + "title");
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
		headerPanel.add(amountLabel, "gap para");
		headerPanel.add(getAmountTextField(), "growx");

		return headerPanel;
	}

	@Override
	public void setup()
	{
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddOperation());
		getContextualToolBar().add(actionProvider.getSaveOperation());
		getContextualToolBar().add(actionProvider.getDeleteOperation());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getEditOperationType());
		getContextualToolBar().add(actionProvider.getEditOperationTypeTypology());

		/* aggiorno la lista degli incarichi */
		getJobComboBox().setModel(new EventComboBoxModel<Job>(jobService.getAllJobs()));

		/* aggiorno la lista dei tipi */
		getTypeComboBox().setModel(
		  new EventComboBoxModel<OperationType>(operationTypeService.getAllOperationTypes()));

		/* aggiorno la tabella */
		String[] properties =
		  new String[] {"description", "job", "operationType", "isVacazione",
		    "isProfessionalVacazione", "operationDate", "amount"};
		String[] columnsName =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.type"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isVacazione"),
		    getResourceMap().getString(
		      LOCALIZATION_PREFIX + "Table.isProfessionalVacazione"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.amount")};
		boolean[] writable =
		  new boolean[] {false, false, false, false, false, false, false};

		tableModel =
		  new EventJXTableModel<Operation>(operationService.getAllOperations(),
		    new BeanTableFormat<Operation>(Operation.class, properties,
		      columnsName, writable));
		getContentTable().setModel(tableModel);

	}

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
		{
			descriptionTextField = new JTextField(20);
			registerComponent(descriptionTextField);
		}
		return descriptionTextField;
	}

	public JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
		{
			jobComboBox = new JComboBox();
			registerComponent(jobComboBox);
		}
		return jobComboBox;
	}

	public JComboBox getTypeComboBox()
	{
		if(typeComboBox == null)
		{
			typeComboBox = new JComboBox();
			registerComponent(typeComboBox);
		}
		return typeComboBox;
	}

	public JCheckBox getVacazioneCheckBox()
	{
		if(vacazioneCheckBox == null)
		{
			vacazioneCheckBox = new JCheckBox();
			registerComponent(vacazioneCheckBox);
		}
		return vacazioneCheckBox;
	}

	public JCheckBox getProfessionalVacazioneCheckBox()
	{
		if(professionalVacazioneCheckBox == null)
		{
			professionalVacazioneCheckBox = new JCheckBox();
			registerComponent(professionalVacazioneCheckBox);
		}
		return professionalVacazioneCheckBox;
	}

	public JXDatePicker getOperationDate()
	{
		if(operationDate == null)
		{
			operationDate = new JXDatePicker();
			registerComponent(operationDate);
		}
		return operationDate;
	}

	public JTextField getAmountTextField()
	{
		if(amountTextField == null)
		{
			amountTextField = new JTextField(10);
			registerComponent(amountTextField);
		}
		return amountTextField;
	}
}
