package it.greentone.persistence;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenToneUtilities;

import java.util.Calendar;
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
	@Inject
	private MetadataService metadataService;

	private final EventList<Document> allDocuments =
	  new BasicEventList<Document>();
	/** Carattere di padding per il protocollo dell'incarico */
	public final Character PROTOCOL_PADDING_CHAR = '0';

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
		/* rendo persistente il documento */
		storeDocument(document);
		/* aggiorno come metadato l'ultimo protocollo usato */
		Metadata metadata =
		  metadataService
		    .getMetadataFromName(MetadataService.DOCUMENT_LAST_PROTOCOL);
		if(metadata != null)
		{
			metadata.setValue(document.getProtocol());
		}
		else
		{
			metadata = new Metadata();
			metadata.setName(MetadataService.DOCUMENT_LAST_PROTOCOL);
			metadata.setValue(document.getProtocol());
			metadata.setType(MetadataType.STRING);
		}
		metadataService.storeMetadata(metadata);
		/* aggiungo nella lista interna il documento */
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

	/**
	 * Restituisce il prossimo protocollo utile da utilizzare. Il protocollo per i
	 * documenti è del tipo 00001-2011
	 * 
	 * @return il prossimo protocollo utile da utilizzare
	 */
	public String getNextProtocol()
	{
		String counter = "";
		int year = Calendar.getInstance().get(Calendar.YEAR);

		/* recupero il metadato dell'ultimo protocollo usato */
		Metadata metadata =
		  metadataService
		    .getMetadataFromName(MetadataService.DOCUMENT_LAST_PROTOCOL);
		if(metadata != null)
		{
			String lastProtocol = metadata.getValue();
			/* recupero l'anno */
			String protocolYear =
			  lastProtocol
			    .substring(ConfigurationProperties.DOCUMENT_PROTOCOL_NUMERIC_LENGHT
			      + ConfigurationProperties.DOCUMENT_PROTOCOL_SEPARATOR.length());

			/* se siamo nello stesso anno */
			if(protocolYear.equals(year + ""))
			{
				/* recupero la parte del contatore */
				int endIndex = ConfigurationProperties.DOCUMENT_PROTOCOL_NUMERIC_LENGHT;
				counter = lastProtocol.substring(0, endIndex);
				/* tolgo il padding iniziale di 0 */
				int i = 0;
				while(counter.charAt(i) == ConfigurationProperties.PROTOCOL_PADDING_CHAR)
				{
					i++;
				}
				counter = counter.substring(i);
				counter = (Integer.valueOf(counter) + 1) + "";
			}
			else
			{
				/* caso primo protocollo dell'anno */
				counter = "1";
			}
		}
		else
		{
			/* caso primo protocollo del sistema */
			counter = "1";
		}

		/* costruisco il protocollo nella versione nnnnn-yyyy */
		String protocol =
		  GreenToneUtilities.leftPadding(counter,
		    ConfigurationProperties.DOCUMENT_PADDING_CHAR,
		    ConfigurationProperties.DOCUMENT_PROTOCOL_NUMERIC_LENGHT);
		protocol =
		  protocol + ConfigurationProperties.DOCUMENT_PROTOCOL_SEPARATOR + year;
		return protocol;
	}
}
