package it.greentone.gui.action;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.OperationsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobStatus;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;

import java.text.ParseException;

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
 * Salva l'operazione di un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveOperationAction extends AbstractBean
{
	@Inject
	private OperationService operationService;
	@Inject
	private OperationsPanel operationsPanel;
	@Inject
	private ConfigurationProperties properties;
	private final ResourceMap resourceMap;
	boolean saveOperationActionEnabled = false;

	/**
	 * Salva l'operazione di un incarico.
	 */
	public SaveOperationAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Salva un'operazione.
	 */
	@Action(enabledProperty = "saveOperationActionEnabled")
	public void saveOperation()
	{
		try
		{
			String errorTitle = resourceMap.getString("ErrorDialog.title");
			/* controllo date */
			DateTime operationDate =
			  GreenToneUtilities.getDateTime(operationsPanel.getOperationDate());
			/*
			 * Issue 34: se il campo data rimane vuoto mostrare un popup che chiede
			 * conferma del salvataggio
			 */
			if(operationDate == null)
			{
				int confirmDialog =
				  JOptionPane.showConfirmDialog(operationsPanel,
				    resourceMap.getString("saveOperation.Action.dateMessage"));
				if(confirmDialog != JOptionPane.OK_OPTION)
					return;
			}
			else
			{
				/* controllo che la data non sia successiva alla data odierna */
				if(operationDate.isAfterNow())
				{
					JOptionPane.showMessageDialog(operationsPanel,
					  resourceMap.getString("saveOperation.Action.dateAfterNowMessage"),
					  errorTitle, JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			/*
			 * Issue 36: se selezionato l'onorario a vacazione allora il valore deve
			 * essere maggiore di 2
			 */
			if(operationsPanel.getVacazioneCheckBox().isSelected())
			{
				String vacazioniValue =
				  GreenToneUtilities
				    .getText(operationsPanel.getNumVacazioniTextField());
				Integer numVacazioni = null;
				if(vacazioniValue == null)
				{
					JOptionPane.showMessageDialog(operationsPanel,
					  resourceMap.getString("saveOperation.Action.vacazioniMessage"),
					  errorTitle, JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					operationsPanel.getNumVacazioniTextField().commitEdit();
					numVacazioni =
					  new Integer(operationsPanel.getNumVacazioniTextField().getValue()
					    .toString());
					if(numVacazioni.intValue() < 2)
					{
						JOptionPane.showMessageDialog(operationsPanel,
						  resourceMap.getString("saveOperation.Action.vacazioniMessage"),
						  errorTitle, JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
			/*
			 * Issue 97: se l'incarico selezionato è in stato chiuso non si possono
			 * modificare le operazioni, se lo stato dell'incarico è sospeso allora
			 * posso inserire solo operazioni di tipo "Acconto"
			 */
			if(operationsPanel.getJobComboBox().getSelectedItem() != null)
			{
				Job selectedJob =
				  (Job) operationsPanel.getJobComboBox().getSelectedItem();
				if(selectedJob.getStatus() == JobStatus.CLOSED)
				{
					/* non si possono modificare le operazioni per un incarico chiuso */
					JOptionPane.showMessageDialog(operationsPanel,
					  resourceMap.getString("saveOperation.Action.closedJobMessage"),
					  errorTitle, JOptionPane.ERROR_MESSAGE);
					return;
				}
				OperationType operationType =
				  OperationType.values()[operationsPanel.getTypeComboBox()
				    .getSelectedIndex()];
				if(selectedJob.getStatus() == JobStatus.SUSPEND
				  && operationType != OperationType.EXPENSE_DEPOSIT)
				{
					/*
					 * si possono aggiungere solo operazioni di acconto per l'incarico in
					 * stato sospeso
					 */
					JOptionPane.showMessageDialog(operationsPanel,
					  resourceMap.getString("saveOperation.Action.suspendJobMessage"),
					  errorTitle, JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			/*
			 * se arrivo qui allora tutta la validazione è passata correttamente e
			 * posso salvare
			 */
			save();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void save() throws ParseException
	{
		/*
		 * se si tratta di una nuova entry creo un nuova operazione altrimenti
		 * modifico quella selezionata
		 */
		Operation operation =
		  operationsPanel.getStatus() == EStatus.EDIT? operationsPanel
		    .getSelectedItem(): new Operation();
		/* compilo il bean */
		if(operationsPanel.getVacazioneCheckBox().isSelected())
		{
			/* caso operazione con onorario a vacazione */
			String vacazioniValue =
			  GreenToneUtilities.getText(operationsPanel.getNumVacazioniTextField());
			Integer numVacazioni = null;
			if(vacazioniValue != null)
			{
				operationsPanel.getNumVacazioniTextField().commitEdit();
				numVacazioni = new Integer(vacazioniValue);
			}
			/*
			 * se ho un valore per le vacazioni allora calcolo anche l'importo
			 * altrimenti lo imposto a null
			 */
			if(numVacazioni == null)
			{
				operation.setNumVacazioni(null);
				operation.setAmount(null);
			}
			else
			{
				operation.setNumVacazioni(numVacazioni);
				/* distinguo i casi di vacazione aiutante o no */
				if(operationsPanel.getProfessionalVacazioneCheckBox().isSelected())
				{
					operation.setAmount(numVacazioni
					  * properties.getVacazioneHelperPrice());
				}
				else
				{
					operation.setAmount(numVacazioni * properties.getVacazionePrice());
				}
			}
		}
		else
		{
			/* caso operazione con importo */
			String amountValue =
			  GreenToneUtilities.getText(operationsPanel.getAmountTextField());
			Double amount = null;
			if(amountValue != null)
			{
				operationsPanel.getAmountTextField().commitEdit();
				amount =
				  new Double(operationsPanel.getAmountTextField().getValue().toString());
				operation.setAmount(GreenToneUtilities.roundTwoDecimals(amount));
			}
			else
			{
				operation.setAmount(null);
			}
			operation.setNumVacazioni(null);
		}

		/* descrizione */
		operation.setDescription(GreenToneUtilities.getText(operationsPanel
		  .getDescriptionTextField()));
		/* flag vacazione aiutante */
		operation.setIsProfessionalVacazione(operationsPanel
		  .getProfessionalVacazioneCheckBox().isSelected());
		/* flag onorario a vacazione */
		operation.setIsVacazione(operationsPanel.getVacazioneCheckBox()
		  .isSelected());
		/* incarico */
		operation.setJob((Job) operationsPanel.getJobComboBox().getSelectedItem());
		/* data operazione */
		operation.setOperationDate(GreenToneUtilities.getDateTime(operationsPanel
		  .getOperationDate()));
		/* tipo di operazione */
		if(operationsPanel.getTypeComboBox().getSelectedIndex() > -1)
		{
			operation.setOperationType(OperationType.values()[operationsPanel
			  .getTypeComboBox().getSelectedIndex()]);
		}
		/* aggiorno la tabella */
		if(operationsPanel.getStatus() == EStatus.NEW)
		{
			operationService.addOperation(operation);
		}
		else
		{
			operationService.storeOperation(operation);
		}
		operationsPanel.clearForm();
		operationsPanel.getContentTable().clearSelection();
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isSaveOperationActionEnabled()
	{
		return saveOperationActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param saveOperationActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setSaveOperationActionEnabled(boolean saveOperationActionEnabled)
	{
		final boolean oldValue = this.saveOperationActionEnabled;
		this.saveOperationActionEnabled = saveOperationActionEnabled;
		firePropertyChange("saveOperationActionEnabled", oldValue,
		  saveOperationActionEnabled);
	}
}
