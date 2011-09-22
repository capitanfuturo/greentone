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
 * </code>
 * <br>
 * <br>
 * Classe di accesso alla tabella {@link OperationType}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("operationTypeDAO")
public class OperationTypeDAO extends JdoDaoSupport
{
	/**
	 * Classe di accesso alla tabella {@link OperationType}
	 * 
	 * @param pmf
	 *          manager della persistenza
	 */
	@Inject
	public OperationTypeDAO(final PersistenceManagerFactory pmf)
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
	public OperationType loadOperationType(final long id)
	  throws DataAccessException
	{
		final OperationType operationType =
		  getJdoTemplate().getObjectById(OperationType.class, Long.valueOf(id));
		if(operationType == null)
			throw new RuntimeException("OperationType " + id + " not found");
		return getPersistenceManager().detachCopy(operationType);
	}

	/**
	 * Rende persistente l'oggetto passato come parametro.
	 * 
	 * @param operationType
	 *          l'oggetto da rendere persistente
	 * @throws DataAccessException
	 */
	public void storeOperationType(final OperationType operationType)
	  throws DataAccessException
	{
		getJdoTemplate().makePersistent(operationType);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param operationType
	 *          l'oggetto da eliminare
	 * @throws DataAccessException
	 */
	public void deleteOperationType(final OperationType operationType)
	  throws DataAccessException
	{
		if(operationType == null || operationType.getId() == null)
			throw new RuntimeException("OperationType is not persistent");
		else
			getPersistenceManager().deletePersistent(operationType);
	}

	/**
	 * Restituisce la lista di tutti gli elementi presenti nel database per questa
	 * tabella.
	 * 
	 * @return la lista di tutti gli elementi presenti nel database per questa
	 *         tabella
	 * @throws DataAccessException
	 */
	public Collection<OperationType> getAllOperationTypes()
	  throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(OperationType.class));
	}
}
