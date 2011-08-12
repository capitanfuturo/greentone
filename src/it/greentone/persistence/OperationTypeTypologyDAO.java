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
 * 
 * @author Giuseppe Caliendo
 */
@Repository("operationTypeTypologyDAO")
public class OperationTypeTypologyDAO extends JdoDaoSupport
{
	@Inject
	public OperationTypeTypologyDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	public OperationTypeTypology loadOperationTypeTypology(final long id)
	  throws DataAccessException
	{
		final OperationTypeTypology operationTypeTypology =
		  getJdoTemplate().getObjectById(OperationTypeTypology.class,
		    Long.valueOf(id));
		if(operationTypeTypology == null)
			throw new RuntimeException("OperationTypeTypology " + id + " not found");
		return getPersistenceManager().detachCopy(operationTypeTypology);
	}

	public void storeOperationTypeTypology(
	  final OperationTypeTypology operationTypeTypology)
	  throws DataAccessException
	{
		getJdoTemplate().makePersistent(operationTypeTypology);
	}

	public void deleteOperationTypeTypology(
	  final OperationTypeTypology operationTypeTypology)
	  throws DataAccessException
	{
		if(operationTypeTypology == null || operationTypeTypology.getId() == null)
			throw new RuntimeException("OperationTypeTypology is not persistent");
		else
			getPersistenceManager().deletePersistent(operationTypeTypology);
	}

	public Collection<OperationTypeTypology> getAllOperationTypeTypologies()
	  throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(OperationTypeTypology.class));
	}
}
