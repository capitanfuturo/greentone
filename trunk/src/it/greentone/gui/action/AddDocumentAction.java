package it.greentone.gui.action;

import it.greentone.gui.AbstractPanel.EStatus;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.persistence.DocumentService;

import javax.inject.Inject;

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
 * Aggiunge un documento.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class AddDocumentAction
{
	@Inject
	private DocumentsPanel documentsPanel;
	@Inject
	private DocumentService documentService;

	/**
	 * Aggiunge un documento.
	 */
	@Action
	public void addDocument()
	{
		documentsPanel.clearForm();
		documentsPanel.setStatus(EStatus.NEW);
		/* calcolo il protocollo da impostare */
		documentsPanel.getProtocolTextField().setText(
		  documentService.getNextProtocol());
	}
}
