package it.greentone.gui.action;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.OperationsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;
import it.greentone.persistence.OperationType;

import javax.inject.Inject;

import org.jdesktop.application.Action;
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
 * Salva un'operazione.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveOperationAction
{
	@Inject
	private OperationService operationService;
	@Inject
	private OperationsPanel operationsPanel;

	/**
	 * Salva un'operazione.
	 */
	@Action
	public void saveOperation()
	{
		/*
		 * se si tratta di una nuova entry creo un nuova operazione altrimenti
		 * modifico quella selezionata
		 */
		Operation operation =
		  operationsPanel.getStatus() == EStatus.EDIT? operationsPanel
		    .getSelectedItem(): new Operation();
		/* compilo il bean */
		String value =
		  GreenToneUtilities.getText(operationsPanel.getAmountTextField());
		Double amount = value != null? Double.valueOf(value): null;
		operation.setAmount(amount);
		operation.setDescription(GreenToneUtilities.getText(operationsPanel
		  .getDescriptionTextField()));
		operation.setIsProfessionalVacazione(operationsPanel
		  .getProfessionalVacazioneCheckBox().isSelected());
		operation.setIsVacazione(operationsPanel.getVacazioneCheckBox()
		  .isSelected());
		operation.setJob((Job) operationsPanel.getJobComboBox().getSelectedItem());
		operation.setOperationDate(new DateTime(operationsPanel.getOperationDate()
		  .getDate()));
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
		operationsPanel.setStatus(EStatus.NEW);
	}
}
