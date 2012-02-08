package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.AbstractPanel.EStatus;
import it.greentone.gui.panel.OperationsPanel;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;

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
 * Elimina l'operazione selezionata in tabella.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class DeleteOperationAction extends AbstractBean
{
	@Inject
	OperationsPanel operationsPanel;
	@Inject
	OperationService operationService;
	boolean deleteOperationActionEnabled = false;
	private final ResourceMap resourceMap;

	/**
	 * Elimina l'operazione selezionata in tabella.
	 */
	public DeleteOperationAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Elimina l'operazione selezionata in tabella.
	 */
	@Action(enabledProperty = "deleteOperationActionEnabled")
	public void deleteOperation()
	{
		int confirmDialog =
		  JOptionPane.showConfirmDialog(operationsPanel,
		    resourceMap.getString("deleteOperation.Action.confirmMessage"));
		if(confirmDialog == JOptionPane.OK_OPTION)
		{
			Operation operation = operationsPanel.getSelectedItem();
			operationService.deleteOperation(operation);
			operationsPanel.clearForm();
			operationsPanel.setStatus(EStatus.NEW);
		}
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isDeleteOperationActionEnabled()
	{
		return deleteOperationActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param deleteOperationActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setDeleteOperationActionEnabled(
	  boolean deleteOperationActionEnabled)
	{
		final boolean oldValue = this.deleteOperationActionEnabled;
		this.deleteOperationActionEnabled = deleteOperationActionEnabled;
		firePropertyChange("deleteOperationActionEnabled", oldValue,
		  deleteOperationActionEnabled);
	}
}
