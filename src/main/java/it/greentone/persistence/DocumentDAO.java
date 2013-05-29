package it.greentone.persistence;

import java.util.Collection;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import org.springframework.stereotype.Repository;

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
 * </code> <br>
 * <br>
 * Classe di accesso alla tabella {@link Document}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("documentDAO")
public class DocumentDAO extends JdoDaoSupport {
    /**
     * Classe di accesso alla tabella {@link Document}
     * 
     * @param pmf
     *            manager della persistenza
     */
    @Inject
    public DocumentDAO(final PersistenceManagerFactory pmf) {
        setPersistenceManagerFactory(pmf);
    }

    /**
     * Restituisce l'oggetto di identificativo passato in ingresso.
     * 
     * @param id
     *            identificativo dell'oggetto
     * @return l'oggetto di identificativo passato in ingresso
     * @throws DataAccessException
     */
    public Document loadDocument(final long id) throws DataAccessException {
        final Document document = getJdoTemplate().getObjectById(Document.class, Long.valueOf(id));
        if (document == null)
            throw new RuntimeException("Document " + id + " not found");
        return getPersistenceManager().detachCopy(document);
    }

    /**
     * Rende persistente l'oggetto passato come parametro.
     * 
     * @param document
     *            l'oggetto da rendere persistente
     * @throws DataAccessException
     */
    public void storeDocument(final Document document) throws DataAccessException {
        getJdoTemplate().makePersistent(document);
    }

    /**
     * Elimina l'oggetto passato in ingresso.
     * 
     * @param document
     *            l'oggetto da eliminare
     * @throws DataAccessException
     */
    public void deleteDocument(final Document document) throws DataAccessException {
        if (document == null || document.getId() == null)
            throw new RuntimeException("Document is not persistent");
        else
            getPersistenceManager().deletePersistent(document);
    }

    /**
     * Restituisce la lista di tutti gli elementi presenti nel database per questa tabella.
     * 
     * @return la lista di tutti gli elementi presenti nel database per questa tabella
     * @throws DataAccessException
     */
    public Collection<Document> getAllDocuments() throws DataAccessException {
        return getPersistenceManager().detachCopyAll(getJdoTemplate().find(Document.class));
    }

    /**
     * Restituisce la lista di documenti appartenenti alla persona passata in ingresso.
     * 
     * @param person
     *            persona
     * @return la lista di documenti appartenenti alla persona passata in ingresso
     * @throws DataAccessException
     */
    public Collection<Document> getDocumentsAsRecipient(Person person) throws DataAccessException {
        Query query =
                getPersistenceManager()
                        .newQuery(
                                "SELECT FROM it.greentone.persistence.Document WHERE recipient == m PARAMETERS it.greentone.persistence.Person m");
        @SuppressWarnings("unchecked")
        Collection<Document> result =
                getPersistenceManager().detachCopyAll((Collection<Document>) query.execute(person));
        return result;
    }

    /**
     * Restituisce la lista di tutti i documenti dell'incarico passato in ingresso.
     * 
     * @param job
     *            incarico
     * @return la lista di tutti i documenti dell'incarico passato in ingresso
     * @throws DataAccessException
     */
    public Collection<Document> getDocumentsJob(Job job) throws DataAccessException {
        Query query =
                getPersistenceManager()
                        .newQuery(
                                "SELECT FROM it.greentone.persistence.Document WHERE job == j PARAMETERS it.greentone.persistence.Job j");
        @SuppressWarnings("unchecked")
        Collection<Document> result = getPersistenceManager().detachCopyAll((Collection<Document>) query.execute(job));
        return result;
    }
}
