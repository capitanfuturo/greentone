package it.greentone.gui.action;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.Person;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
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
 * Salva un documento.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveDocumentAction extends AbstractBean
{
	@Inject
	private DocumentsPanel documentsPanel;
	@Inject
	private DocumentService documentService;
	boolean saveDocumentActionEnabled = false;

	/**
	 * Salva un documento.
	 */
	@Action(enabledProperty = "saveDocumentActionEnabled")
	public void saveDocument()
	{
		/*
		 * se si tratta di una nuova entry creo un nuovo documento altrimenti
		 * modifico quella selezionata
		 */
		Document document =
		  documentsPanel.getStatus() == EStatus.EDIT? documentsPanel
		    .getSelectedItem(): new Document();
		/* compilo il bean */
		document.setDescription(GreenToneUtilities.getText(documentsPanel
		  .getDescriptionTextField()));
		document.setIsDigital(documentsPanel.getIsDigitalCheckBox().isSelected());
		document.setIsIncoming(documentsPanel.getIncomingCheckBox().isSelected());
		document.setJob((Job) documentsPanel.getJobComboBox().getSelectedItem());
		document.setNotes(documentsPanel.getNotesTextArea().getText());
		document.setProtocol(GreenToneUtilities.getText(documentsPanel
		  .getProtocolTextField()));
		document.setRecipient((Person) documentsPanel.getRecipientComboBox()
		  .getSelectedItem());
		document.setReleaseDate(GreenToneUtilities.getDateTime(documentsPanel
		  .getReleaseDateDatePicker()));
		document.setUri(GreenToneUtilities.getText(documentsPanel
		  .getFileTextField()));
		/* aggiorno la tabella */
		if(documentsPanel.getStatus() == EStatus.NEW)
			documentService.addDocument(document);
		else
			documentService.storeDocument(document);
		documentsPanel.clearForm();
		documentsPanel.getContentTable().clearSelection();
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isSaveDocumentActionEnabled()
	{
		return saveDocumentActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param saveDocumentActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setSaveDocumentActionEnabled(boolean saveDocumentActionEnabled)
	{
		final boolean oldValue = this.saveDocumentActionEnabled;
		this.saveDocumentActionEnabled = saveDocumentActionEnabled;
		firePropertyChange("saveDocumentActionEnabled", oldValue,
		  saveDocumentActionEnabled);
	}
}
