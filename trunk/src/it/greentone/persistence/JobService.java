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
 * </code>
 * <br>
 * <br>
 * Classe di supporto per l'accesso e gestione di {@link Job}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("jobService")
@Transactional(propagation = Propagation.REQUIRED)
public class JobService
{
	@Inject
	private JobDAO jobDAO;
	private final EventList<Job> allJobs = new BasicEventList<Job>();

	/**
	 * Restitusice l'oggetto di identificativo passato in ingresso.
	 * 
	 * @param id
	 *          identificativo da caricare
	 * @return l'oggetto di identificativo passato in ingresso
	 */
	public Job loadJob(long id)
	{
		return jobDAO.loadJob(id);
	}

	/**
	 * Rende persistente un oggetto nel database.
	 * 
	 * @param job
	 *          oggetto da rendere persistente
	 */
	public void storeJob(final Job job)
	{
		jobDAO.storeJob(job);
	}

	/**
	 * Aggiunge un nuovo oggetto nel database
	 * 
	 * @param job
	 *          l'incarico da aggiungere
	 */
	public void addJob(Job job)
	{
		storeJob(job);
		allJobs.add(job);
	}

	/**
	 * Elimina un oggetto dal database
	 * 
	 * @param job
	 *          l'incarico da eliminare
	 */
	public void deleteJob(final Job job)
	{
		jobDAO.deleteJob(job);
		allJobs.remove(job);
	}

	/**
	 * Restituisce la lista di tutti gli incarichi presenti in database.
	 * 
	 * @return la lista di tutti gli incarichi presenti in database
	 * @throws DataAccessException
	 */
	public EventList<Job> getAllJobs() throws DataAccessException
	{
		if(allJobs.isEmpty())
			allJobs.addAll(jobDAO.getAllJobs());
		return allJobs;
	}
}
