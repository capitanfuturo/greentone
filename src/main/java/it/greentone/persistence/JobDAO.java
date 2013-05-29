package it.greentone.persistence;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
 * Classe di accesso alla tabella {@link Job}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("jobDAO")
public class JobDAO extends JdoDaoSupport {
    /**
     * Classe di accesso alla tabella {@link Job}
     * 
     * @param pmf
     *            manager della persistenza
     */
    @Inject
    public JobDAO(final PersistenceManagerFactory pmf) {
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
    public Job loadJob(final long id) throws DataAccessException {
        final Job job = getJdoTemplate().getObjectById(Job.class, Long.valueOf(id));
        if (job == null)
            throw new RuntimeException("Job " + id + " not found");
        return getPersistenceManager().detachCopy(job);
    }

    /**
     * Rende persistente l'oggetto passato in ingresso.
     * 
     * @param job
     * @throws DataAccessException
     */
    public void storeJob(final Job job) throws DataAccessException {
        getJdoTemplate().makePersistent(job);
    }

    /**
     * Elimina l'oggetto passato in ingresso.
     * 
     * @param job
     *            l'oggetto da eliminare
     * @throws DataAccessException
     */
    public void deleteJob(final Job job) throws DataAccessException {
        if (job == null || job.getId() == null)
            throw new RuntimeException("Job is not persistent");
        else
            getPersistenceManager().deletePersistent(job);
    }

    /**
     * Restituisce la lista di tutti gli elementi presenti in database.
     * 
     * @return la lista di tutti gli elementi presenti in database
     * @throws DataAccessException
     */
    public Collection<Job> getAllJobs() throws DataAccessException {
        return getPersistenceManager().detachCopyAll(getJdoTemplate().find(Job.class));
    }

    /**
     * Restituisce la lista di incarichi per i quali la persona passata in ingresso sia il responsabile.
     * 
     * @param manager
     *            responsabile dell'incarico
     * @return la lista di incarichi per i quali la persona passata in ingresso sia il responsabile
     * @throws DataAccessException
     */
    public Collection<Job> getJobsAsManager(Person manager) throws DataAccessException {
        Query query =
                getPersistenceManager().newQuery(
                        "SELECT FROM it.greentone.persistence.Job "
                                + "WHERE manager == m PARAMETERS it.greentone.persistence.Person m");
        @SuppressWarnings("unchecked")
        Collection<Job> result = getPersistenceManager().detachCopyAll((Collection<Job>) query.execute(manager));
        return result;
    }

    /**
     * Restituisce la lista di incarichi per i quali la persona passata in ingresso sia il committente.
     * 
     * @param customer
     *            committente dell'incarico
     * @return la lista di incarichi per i quali la persona passata in ingresso sia il committente
     * @throws DataAccessException
     */
    public Collection<Job> getJobsAsCustomer(Person customer) throws DataAccessException {
        Query query =
                getPersistenceManager().newQuery(
                        "SELECT FROM it.greentone.persistence.Job "
                                + "WHERE customer == m PARAMETERS it.greentone.persistence.Person m");
        @SuppressWarnings("unchecked")
        Collection<Job> result = getPersistenceManager().detachCopyAll((Collection<Job>) query.execute(customer));
        return result;
    }

    /**
     * Restituisce l'insieme dei comuni di riferimento presenti nella tabella degli incarichi.
     * 
     * @return l'insieme dei comuni di riferimento presenti nella tabella degli incarichi
     */
    public Collection<String> getAllCities() {
        Query query = getPersistenceManager().newQuery(Job.class);
        @SuppressWarnings("unchecked")
        Collection<Job> result = getPersistenceManager().detachCopyAll((Collection<Job>) query.execute());
        Set<String> cities = new HashSet<String>();
        for (Job job : result) {
            cities.add(job.getCity());
        }
        return cities;
    }
}
