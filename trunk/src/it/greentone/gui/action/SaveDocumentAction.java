package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.GreenToneAppConfig;
import it.greentone.GreenToneLogProvider;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.ModelEventManager;
import it.greentone.gui.panel.AbstractPanel.EStatus;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.Person;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

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
 * Salva un documento.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveDocumentAction extends AbstractBean {
	@Inject
	private DocumentsPanel documentsPanel;
	@Inject
	private DocumentService documentService;
	@Inject
	private GreenToneLogProvider logger;
	@Inject
	private GreenToneUtilities utilities;
	@Inject
	private ModelEventManager modelEventManager;
	private final ResourceMap resourceMap;
	boolean saveDocumentActionEnabled = false;

	/**
	 * Salva un documento.
	 */
	public SaveDocumentAction() {
		resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Salva un documento.
	 */
	@Action(enabledProperty = "saveDocumentActionEnabled")
	public void saveDocument() {
		/* controllo che la data inserita non sia nel futuro */
		DateTime releaseDate = GreenToneUtilities.getDateTime(documentsPanel.getReleaseDateDatePicker());
		if (releaseDate != null && releaseDate.isAfterNow()) {
			JOptionPane.showMessageDialog(documentsPanel, resourceMap.getString("saveDocument.Action.dateAfterNowMessage"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		/*
		 * se si tratta di una nuova entry creo un nuovo documento altrimenti
		 * modifico quella selezionata
		 */
		Document document = documentsPanel.getStatus() == EStatus.EDIT ? documentsPanel.getSelectedItem() : new Document();
		/* compilo il bean */
		document.setDescription(GreenToneUtilities.getText(documentsPanel.getDescriptionTextField()));
		document.setIsDigital(documentsPanel.getIsDigitalCheckBox().isSelected());
		document.setIsOutgoing(documentsPanel.getOutgoingCheckBox().isSelected());
		document.setJob((Job) documentsPanel.getJobComboBox().getSelectedItem());
		document.setNotes(documentsPanel.getNotesTextArea().getText());
		String protocol = GreenToneUtilities.getText(documentsPanel.getProtocolTextField());
		document.setProtocol(protocol);
		document.setRecipient((Person) documentsPanel.getRecipientComboBox().getSelectedItem());
		document.setReleaseDate(releaseDate);

		/* salvataggio dei file */
		String oldUri = document.getUri();
		File file = documentsPanel.getFile();
		String newUri = null;

		try {
			/* caso nessun file precedentemente caricato */
			if (oldUri == null) {
				if (file != null)
					newUri = copyFile(file, protocol);
			} else {
				/* caso file precedentemente caricato */
				File oldFile = new File(oldUri);
				oldFile.delete();
				if (file != null) {
					newUri = copyFile(file, protocol);
				}
			}
			document.setUri(newUri);
		} catch (Exception e) {
			logger.getLogger().log(Level.WARNING, resourceMap.getString("ErrorMessage.copyingFile") + file.getPath(), e);
		}

		/* aggiorno la tabella */
		if (documentsPanel.getStatus() == EStatus.NEW) {
			documentService.addDocument(document);
			modelEventManager.fireDocumentInserted(document);
		} else {
			documentService.storeDocument(document);
			modelEventManager.fireDocumentModified(document);
		}
		documentsPanel.postSaveData();
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isSaveDocumentActionEnabled() {
		return saveDocumentActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param saveDocumentActionEnabled
	 *            <code>true</code> se si vuole abilitare l'azione,
	 *            <code>false</code> altrimenti
	 */
	public void setSaveDocumentActionEnabled(boolean saveDocumentActionEnabled) {
		final boolean oldValue = this.saveDocumentActionEnabled;
		this.saveDocumentActionEnabled = saveDocumentActionEnabled;
		firePropertyChange("saveDocumentActionEnabled", oldValue, saveDocumentActionEnabled);
	}

	/**
	 * Copia il nuovo file nel repository di GreenTone e restituisce l'URI del
	 * nuovo file
	 * 
	 * @param inputFile
	 *            file da copiare nel repository
	 * @return l'URI del nuovo file copiato
	 * @throws IOException
	 */
	private String copyFile(File inputFile, String protocol) throws IOException {
		new File(GreenToneAppConfig.DOCUMENTS_REPOSITORY).mkdirs();
		String filename = inputFile.getName();
		String prefix = filename;
		String extension = "";
		if (filename.length() > 4) {
			prefix = filename.substring(0, filename.length() - 4);
			extension = filename.substring(filename.length() - 4);
		}
		File copiedFile = new File(GreenToneAppConfig.DOCUMENTS_REPOSITORY + prefix + "_" + protocol + extension);
		utilities.copyFile(inputFile, copiedFile);
		return copiedFile.getCanonicalPath().toString();
	}
}
