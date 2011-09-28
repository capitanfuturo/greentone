package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.OperationsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
		/*
		 * Issue 34: se il campo data rimane vuoto mostrare un popup che chiede
		 * conferma del salvataggio
		 */
		if(GreenToneUtilities.getDateTime(operationsPanel.getOperationDate()) == null)
		{
			int confirmDialog =
			  JOptionPane.showConfirmDialog(operationsPanel,
			    resourceMap.getString("saveOperation.Action.dateMessage"));
			if(confirmDialog == JOptionPane.OK_OPTION)
			{
				save();
			}
		}
		else
		{
			save();
		}
	}

	private void save()
	{
		/*
		 * se si tratta di una nuova entry creo un nuova operazione altrimenti
		 * modifico quella selezionata
		 */
		Operation operation =
		  operationsPanel.getStatus() == EStatus.EDIT? operationsPanel
		    .getSelectedItem(): new Operation();
		/* compilo il bean */
		Object value = operationsPanel.getAmountTextField().getValue();
		Double amount = null;
		if(value != null)
		{
			amount = new Double(value.toString());
		}
		/* Issue 33: se si tratta di vacazioni allora il numero imputato è un intero */
		if(operationsPanel.getVacazioneCheckBox().isSelected())
		{
			operation.setAmount(new Double(amount.intValue()));
		}
		else
		{
			operation.setAmount(GreenToneUtilities.roundTwoDecimals(amount));
		}
		operation.setDescription(GreenToneUtilities.getText(operationsPanel
		  .getDescriptionTextField()));
		operation.setIsProfessionalVacazione(operationsPanel
		  .getProfessionalVacazioneCheckBox().isSelected());
		operation.setIsVacazione(operationsPanel.getVacazioneCheckBox()
		  .isSelected());
		operation.setJob((Job) operationsPanel.getJobComboBox().getSelectedItem());
		operation.setOperationDate(GreenToneUtilities.getDateTime(operationsPanel
		  .getOperationDate()));
		operation.setOperationType((OperationType) operationsPanel
		  .getTypeComboBox().getSelectedItem());
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
