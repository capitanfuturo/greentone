package it.greentone.persistence;

import java.util.Collection;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;

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
 * Classe di accesso alla tabella {@link JobCategory}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("jobCategoryDAO")
public class JobCategoryDAO extends JdoDaoSupport {
    /**
     * Classe di accesso alla tabella {@link JobCategoryDAO}
     * 
     * @param pmf
     *            manager della persistenza
     */
    @Inject
    public JobCategoryDAO(final PersistenceManagerFactory pmf) {
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
    public JobCategory loadJobCategory(final long id) throws DataAccessException {
        final JobCategory jobCategory = getJdoTemplate().getObjectById(JobCategory.class, Long.valueOf(id));
        if (jobCategory == null)
            throw new RuntimeException("Job category " + id + " not found");
        return getPersistenceManager().detachCopy(jobCategory);
    }

    /**
     * Rende persistente l'oggetto passato come parametro.
     * 
     * @param jobCategory
     *            l'oggetto da rendere persistente
     * @throws DataAccessException
     */
    public void storeJobCategory(final JobCategory jobCategory) throws DataAccessException {
        getJdoTemplate().makePersistent(jobCategory);
    }

    /**
     * Elimina l'oggetto passato in ingresso.
     * 
     * @param jobCategory
     *            l'oggetto da eliminare
     * @throws DataAccessException
     */
    public void deleteJobCategory(final JobCategory jobCategory) throws DataAccessException {
        if (jobCategory == null || jobCategory.getId() == null)
            throw new RuntimeException("Job category is not persistent");
        else
            getPersistenceManager().deletePersistent(jobCategory);
    }

    /**
     * Restituisce la lista di tutti gli elementi presenti nel database per questa tabella.
     * 
     * @return la lista di tutti gli elementi presenti nel database per questa tabella
     * @throws DataAccessException
     */
    public Collection<JobCategory> getAllJobCategories() throws DataAccessException {
        return getPersistenceManager().detachCopyAll(getJdoTemplate().find(JobCategory.class));
    }
}
