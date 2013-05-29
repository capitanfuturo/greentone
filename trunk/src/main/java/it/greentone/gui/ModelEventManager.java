package it.greentone.gui;

import it.greentone.persistence.Document;
import it.greentone.persistence.Job;
import it.greentone.persistence.Operation;
import it.greentone.persistence.Person;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2012 GreenTone Developer Team.<br>
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
 * Accentratore degli eventi che possono coinvolgere diverse schede dell'interfaccia utente.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ModelEventManager {

    private final PropertyChangeSupport support;
    /** Evento: operazione inserita */
    public static final String OPERATION_INSERTED = "OPERATION_INSERTED";
    /** Evento: operazione modificata */
    public static final String OPERATION_MODIFIED = "OPERATION_MODIFIED";
    /** Evento: operazione eliminata */
    public static final String OPERATION_DELETED = "OPERATION_DELETED";
    /** Evento: documento inserito */
    public static final String DOCUMENT_INSERTED = "DOCUMENT_INSERTED";
    /** Evento: documento modificato */
    public static final String DOCUMENT_MODIFIED = "DOCUMENT_MODIFIED";
    /** Evento: documento eliminato */
    public static final String DOCUMENT_DELETED = "DOCUMENT_DELETED";
    /** Evento: incarico inserito */
    public static final String JOB_INSERTED = "JOB_INSERTED";
    /** Evento: incarico modificato */
    public static final String JOB_MODIFIED = "JOB_MODIFIED";
    /** Evento: incarico eliminato */
    public static final String JOB_DELETED = "JOB_DELETED";
    /** Evento: anagrafica modificata */
    public static final String PERSON_MODIFIED = "PERSON_MODIFIED";

    /**
     * Accentratore degli eventi che possono coinvolgere diverse schede dell'interfaccia utente.
     */
    public ModelEventManager() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * Aggiunge un listener a questa classe.
     * 
     * @param listener
     *            listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Rimuove un listener a questa classe.
     * 
     * @param listener
     *            listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Comunica l'inserimento di una nuova operazione
     * 
     * @param operation
     *            operazione inserita
     */
    public void fireOperationInserted(Operation operation) {
        support.firePropertyChange(OPERATION_INSERTED, null, operation);
    }

    /**
     * Comunica la modifica di un'operazione.
     * 
     * @param newValue
     *            operazione modificata
     */
    public void fireOperationModified(Operation newValue) {
        support.firePropertyChange(OPERATION_MODIFIED, null, newValue);
    }

    /**
     * Comunica l'eliminazione di un'operazione
     * 
     * @param operation
     *            operation eliminata
     */
    public void fireOperationDeleted(Operation operation) {
        support.firePropertyChange(OPERATION_DELETED, null, operation);
    }

    /**
     * Comunica l'inserimento di un documento
     * 
     * @param document
     *            documento inserito
     */
    public void fireDocumentInserted(Document document) {
        support.firePropertyChange(DOCUMENT_INSERTED, null, document);
    }

    /**
     * Comunica la modifica di un documento
     * 
     * @param document
     *            documento modificato
     */
    public void fireDocumentModified(Document document) {
        support.firePropertyChange(DOCUMENT_MODIFIED, null, document);
    }

    /**
     * Comunica l'eliminazione di un documento
     * 
     * @param document
     *            documento eliminato
     */
    public void fireDocumentDeleted(Document document) {
        support.firePropertyChange(DOCUMENT_DELETED, null, document);
    }

    /**
     * Comunica l'inserimento di un incarico
     * 
     * @param job
     *            incarico inserito
     */
    public void fireJobInserted(Job job) {
        support.firePropertyChange(JOB_INSERTED, null, job);
    }

    /**
     * Comunica la modifica di un incarico
     * 
     * @param job
     *            incarico modificato
     */
    public void fireJobModified(Job job) {
        support.firePropertyChange(JOB_MODIFIED, null, job);
    }

    /**
     * Comunica l'eliminazione di un incarico
     * 
     * @param job
     *            incarico eliminato
     */
    public void fireJobDeleted(Job job) {
        support.firePropertyChange(JOB_DELETED, null, job);
    }

    /**
     * Comunica la modifica di un'anagrafica
     * 
     * @param person
     *            anagrafica modificata
     */
    public void firePersonModified(Person person) {
        support.firePropertyChange(PERSON_MODIFIED, null, person);
    }
}
