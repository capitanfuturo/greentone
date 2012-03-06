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
 * Classe di accesso alla tabella degli stakeholder, ovvero interessati ad un
 * certo incarico.
 * 
 * @author Giuseppe Caliendo
 */
@Repository("stakeholderDAO")
public class StakeholderDAO extends JdoDaoSupport
{
	/**
	 * Classe di accesso alla tabella degli stakeholder, ovvero interessati ad un
	 * certo incarico.
	 * 
	 * @param pmf
	 *          manager della persistenza
	 */
	@Inject
	public StakeholderDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	/**
	 * Cerca e restituisce lo stakholder con identificativo id.
	 * 
	 * @param id
	 *          identificativo dello stakeholder
	 * @return eventuale stakeholder identificato dall'id passato in ingresso
	 * @throws DataAccessException
	 */
	public Stakeholder loadStakeholder(final long id) throws DataAccessException
	{
		final Stakeholder stakeholder =
		  getJdoTemplate().getObjectById(Stakeholder.class, Long.valueOf(id));
		if(stakeholder == null)
			throw new RuntimeException("Stakeholder " + id + " not found");
		return getPersistenceManager().detachCopy(stakeholder);
	}

	/**
	 * Aggiunge un nuovo oggetto nell'insieme di quelli persistenti.
	 * 
	 * @param stakeholder
	 *          l'interessato all'incarico
	 * @throws DataAccessException
	 */
	public void storeStakeholder(final Stakeholder stakeholder)
	  throws DataAccessException
	{
		getJdoTemplate().makePersistent(stakeholder);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param stakeholder
	 *          l'interessato all'incarico
	 * @throws DataAccessException
	 */
	public void deleteStakeholder(final Stakeholder stakeholder)
	  throws DataAccessException
	{
		if(stakeholder == null || stakeholder.getId() == null)
			throw new RuntimeException("Stakeholder is not persistent");
		else
			getPersistenceManager().deletePersistent(stakeholder);
	}

	/**
	 * Restituisce la lista degli elementi presenti in database.
	 * 
	 * @return la lista degli elementi presenti in database
	 * @throws DataAccessException
	 */
	public Collection<Stakeholder> getAllStakeholders()
	  throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(Stakeholder.class));
	}
}
