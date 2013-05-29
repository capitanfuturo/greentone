package it.greentone.persistence;

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
 * </code> <br>
 * <br>
 * Classe di supporto per l'accesso e gestione di {@link JobCategory}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("jobCategoryService")
@Transactional(propagation = Propagation.REQUIRED)
public class JobCategoryService {
    @Inject
    private JobCategoryDAO jobCategoryDAO;
    private final EventList<JobCategory> allJobCategories = new BasicEventList<JobCategory>();

    /**
     * Rende persistente un oggetto nel database.
     * 
     * @param jobCategory
     *            oggetto da rendere persistente
     */
    public void storeJobCategory(final JobCategory jobCategory) {
        jobCategoryDAO.storeJobCategory(jobCategory);
    }

    /**
     * Aggiunge un nuovo oggetto nell'insieme di quelli persistenti.
     * 
     * @param jobCategory
     *            categoria di incarico
     */
    public void addJobCategory(JobCategory jobCategory) {
        storeJobCategory(jobCategory);
        allJobCategories.add(jobCategory);
    }

    /**
     * Elimina l'oggetto passato in ingresso.
     * 
     * @param jobCategory
     *            l'oggetto da eliminare
     */
    public void deleteJobCategory(final JobCategory jobCategory) {
        jobCategoryDAO.deleteJobCategory(jobCategory);
        allJobCategories.remove(jobCategory);
    }

    /**
     * Restituisce la lista di tutti gli oggetti presenti in database.
     * 
     * @return la lista di tutti gli oggetti presenti in database
     * @throws DataAccessException
     */
    public EventList<JobCategory> getAllJobCategories() throws DataAccessException {
        if (allJobCategories.isEmpty())
            allJobCategories.addAll(jobCategoryDAO.getAllJobCategories());
        return allJobCategories;
    }
}
