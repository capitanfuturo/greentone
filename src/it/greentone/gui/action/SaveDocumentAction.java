package it.greentone.gui.action;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.Person;

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
 * Salva un documento.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveDocumentAction
{
	@Inject
	DocumentsPanel documentsPanel;
	@Inject
	DocumentService documentService;

	/**
	 * Salva un documento.
	 */
	@Action
	public void saveDocument()
	{
		/*
		 * se si tratta di una nuova entry creo un nuovo documento altrimenti
		 * modifico quella selezionata
		 */
		Document document =
		  documentsPanel.getSelectedDocument() != null? documentsPanel
		    .getSelectedDocument(): new Document();
		/* compilo il bean */
		document.setDescription(GreenToneUtilities.getText(documentsPanel
		  .getDescriptionTextField()));
		document.setIsDigital(documentsPanel.getIsDigitalCheckBox().isSelected());
		document.setIsIncoming(documentsPanel.getIncomingCheckBox().isSelected());
		document.setJob((Job)documentsPanel.getJobComboBox().getSelectedItem());
		document.setNotes(documentsPanel.getNotesTextArea().getText());
		document.setProtocol(GreenToneUtilities.getText(documentsPanel
		  .getProtocolTextField()));
		document.setRecipient((Person)documentsPanel.getRecipientComboBox().getSelectedItem());
		DateTime releaseDate =
		  new DateTime(documentsPanel.getReleaseDateDatePicker().getDate());
		document.setReleaseDate(releaseDate);
		/* rendo persistente il bean */
		documentService.storeDocument(document);
		/* aggiorno la tabella */
		if(documentsPanel.isNewDocument()){
			documentsPanel.getDocumentsEventList().add(document);
		}
		documentsPanel.setNewDocument(true);
	}
}
