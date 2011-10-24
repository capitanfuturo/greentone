package it.greentone.persistence;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

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
 * Classe di supporto per l'accesso e gestione di {@link Document}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("documentService")
@Transactional(propagation = Propagation.REQUIRED)
public class DocumentService
{
	@Inject
	private DocumentDAO documentDAO;
	private final EventList<Document> allDocuments =
	  new BasicEventList<Document>();

	/**
	 * Rende persistente un oggetto nel database.
	 * 
	 * @param document
	 *          oggetto da rendere persistente
	 */
	public void storeDocument(final Document document)
	{
		documentDAO.storeDocument(document);
	}

	/**
	 * Aggiunge un nuovo oggetto nell'insieme di quelli persistenti.
	 * 
	 * @param document
	 *          documento
	 */
	public void addDocument(Document document)
	{
		storeDocument(document);
		allDocuments.add(document);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param document
	 *          l'oggetto da eliminare
	 */
	public void deleteDocument(final Document document)
	{
		documentDAO.deleteDocument(document);
		allDocuments.remove(document);
	}

	/**
	 * Restituisce la lista di tutti gli oggetti presenti in database.
	 * 
	 * @return la lista di tutti gli oggetti presenti in database
	 * @throws DataAccessException
	 */
	public EventList<Document> getAllDocuments() throws DataAccessException
	{
		if(allDocuments.isEmpty())
			allDocuments.addAll(documentDAO.getAllDocuments());
		return allDocuments;
	}

	/**
	 * Restituisce la lista dei documenti dell'incarico passato in ingresso.
	 * 
	 * @param job
	 *          incarico
	 * @return la lista dei documenti dell'incarico passato in ingresso
	 */
	public Collection<Document> getDocumentsJob(Job job)
	{
		return documentDAO.getDocumentsJob(job);
	}

	/**
	 * Restituisce la lista di documenti appartenenti alla persona passata in
	 * ingresso.
	 * 
	 * @param person
	 *          persona
	 * @return la lista di documenti appartenenti alla persona passata in ingresso
	 */
	public Collection<Document> getDocumentsAsRecipient(Person person)
	{
		return documentDAO.getDocumentsAsRecipient(person);
	}
}
