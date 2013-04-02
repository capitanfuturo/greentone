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
 * Classe di accesso alla tabella {@link Metadata}
 * 
 * @author Giuseppe Caliendo
 */
@Repository("metadataDAO")
public class MetadataDAO extends JdoDaoSupport
{
	/**
	 * Classe di accesso alla tabella {@link Metadata}
	 * 
	 * @param pmf
	 *          manager della persistenza
	 */
	@Inject
	public MetadataDAO(final PersistenceManagerFactory pmf)
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
	public Metadata loadMetadata(final long id) throws DataAccessException
	{
		final Metadata metadata =
		  getJdoTemplate().getObjectById(Metadata.class, Long.valueOf(id));
		if(metadata == null)
			throw new RuntimeException("Metadata category " + id + " not found");
		return getPersistenceManager().detachCopy(metadata);
	}

	/**
	 * Rende persistente l'oggetto passato come parametro.
	 * 
	 * @param metadata
	 *          l'oggetto da rendere persistente
	 * @throws DataAccessException
	 */
	public void storeMetadata(final Metadata metadata) throws DataAccessException
	{
		getJdoTemplate().makePersistent(metadata);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param metadata
	 *          l'oggetto da eliminare
	 * @throws DataAccessException
	 */
	public void deleteMetadata(final JobCategory metadata)
	  throws DataAccessException
	{
		if(metadata == null || metadata.getId() == null)
			throw new RuntimeException("Metadata is not persistent");
		else
			getPersistenceManager().deletePersistent(metadata);
	}

	/**
	 * Restituisce la lista di tutti gli elementi presenti nel database per questa
	 * tabella.
	 * 
	 * @return la lista di tutti gli elementi presenti nel database per questa
	 *         tabella
	 * @throws DataAccessException
	 */
	public Collection<Metadata> getAllMetadata() throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(Metadata.class));
	}

	/**
	 * Restituisce il metadato a partire dal nome univoco della property.
	 * 
	 * @param name
	 *          il nome della property da cercare
	 * @return il metadato a partire dal nome univoco della property
	 */
	public Metadata getMetadataFromName(String name)
	{
		Query query =
		  getPersistenceManager()
		    .newQuery(
		      "SELECT FROM it.greentone.persistence.Metadata WHERE name == n PARAMETERS String n");
		@SuppressWarnings("unchecked")
		Collection<Metadata> result =
		  getPersistenceManager().detachCopyAll(
		    (Collection<Metadata>) query.execute(name));
		return result != null && !result.isEmpty()? result.iterator().next(): null;
	}
}
