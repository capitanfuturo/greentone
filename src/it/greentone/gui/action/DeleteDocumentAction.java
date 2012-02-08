package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.AbstractPanel.EStatus;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;

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
 * Elimina un documento.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class DeleteDocumentAction extends AbstractBean
{
	@Inject
	DocumentsPanel documentsPanel;
	@Inject
	DocumentService documentService;
	boolean deleteDocumentActionEnabled = false;
	private final ResourceMap resourceMap;

	/**
	 * Elimina un documento.
	 */
	public DeleteDocumentAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Elimina un documento.
	 */
	@Action(enabledProperty = "deleteDocumentActionEnabled")
	public void deleteDocument()
	{
		int confirmDialog =
		  JOptionPane.showConfirmDialog(documentsPanel,
		    resourceMap.getString("deleteDocument.Action.confirmMessage"));
		if(confirmDialog == JOptionPane.OK_OPTION)
		{
			Document document = documentsPanel.getSelectedItem();
			documentService.deleteDocument(document);
			documentsPanel.clearForm();
			documentsPanel.setStatus(EStatus.NEW);
		}
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isDeleteDocumentActionEnabled()
	{
		return deleteDocumentActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param deleteDocumentActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setDeleteDocumentActionEnabled(boolean deleteDocumentActionEnabled)
	{
		final boolean oldValue = this.deleteDocumentActionEnabled;
		this.deleteDocumentActionEnabled = deleteDocumentActionEnabled;
		firePropertyChange("deleteDocumentActionEnabled", oldValue,
		  deleteDocumentActionEnabled);
	}
}
