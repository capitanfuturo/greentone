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
 * </code>
 * <br>
 * <br>
 * Classe di accesso alla tabella {@link Operation}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("operationDAO")
public class OperationDAO extends JdoDaoSupport
{
	/**
	 * Classe di accesso alla tabella {@link Operation}
	 * 
	 * @param pmf
	 *          manager della persistenza
	 */
	@Inject
	public OperationDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	/**
	 * Restituisce l'oggetto di identificativo passato in ingresso.
	 * 
	 * @param id
	 *          identificativo dell'oggetto
	 * @return l'oggetto di identificativo passato in ingresso
	 * @throws DataAccessException
	 */
	public Operation loadOperation(final long id) throws DataAccessException
	{
		final Operation operation =
		  getJdoTemplate().getObjectById(Operation.class, Long.valueOf(id));
		if(operation == null)
			throw new RuntimeException("Operation " + id + " not found");
		return getPersistenceManager().detachCopy(operation);
	}

	/**
	 * Rende persistente l'oggetto passato come parametro.
	 * 
	 * @param operation
	 *          l'oggetto da rendere persistente
	 * @throws DataAccessException
	 */
	public void storeOperation(final Operation operation)
	  throws DataAccessException
	{
		getJdoTemplate().makePersistent(operation);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param operation
	 *          l'oggetto da eliminare
	 * @throws DataAccessException
	 */
	public void deleteOperation(final Operation operation)
	  throws DataAccessException
	{
		if(operation == null || operation.getId() == null)
			throw new RuntimeException("Operation is not persistent");
		else
			getPersistenceManager().deletePersistent(operation);
	}

	/**
	 * Restituisce la lista di tutti gli elementi presenti nel database per questa
	 * tabella.
	 * 
	 * @return la lista di tutti gli elementi presenti nel database per questa
	 *         tabella
	 * @throws DataAccessException
	 */
	public Collection<Operation> getAllOperations() throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(Operation.class));
	}

	/**
	 * Restituisce la lista delle operazioni dell'incarico passato in ingresso.
	 * 
	 * @param job
	 *          incarico
	 * @return la lista delle operazioni dell'incarico passato in ingresso
	 * @throws DataAccessException
	 */
	public Collection<Operation> getOperationsJob(Job job)
	  throws DataAccessException
	{
		Query query =
		  getPersistenceManager()
		    .newQuery(
		      "SELECT FROM it.greentone.persistence.Operation WHERE job == j PARAMETERS it.greentone.persistence.Job j");
		@SuppressWarnings("unchecked")
		Collection<Operation> result =
		  getPersistenceManager().detachCopyAll(
		    (Collection<Operation>) query.execute(job));
		return result;
	}
}
