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
 * Classe di accessso alla tabella Person nel database.
 * 
 * @author Giuseppe Caliendo
 */
@Repository("personDAO")
public class PersonDAO extends JdoDaoSupport
{
	/**
	 * Classe di accessso alla tabella Person nel database.
	 * 
	 * @param pmf
	 *          manager della persistenza
	 */
	@Inject
	public PersonDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	/**
	 * Restituice la persona con id passato in ingresso.
	 * 
	 * @param id
	 *          identificativo della persona
	 * @return la persona con id passato in ingresso
	 * @throws DataAccessException
	 */
	public Person loadPerson(final long id) throws DataAccessException
	{
		final Person person =
		  getJdoTemplate().getObjectById(Person.class, Long.valueOf(id));
		if(person == null)
			throw new RuntimeException("Person " + id + " not found");
		return getPersistenceManager().detachCopy(person);
	}

	/**
	 * Rende persistente l'oggetto passato come parametro.
	 * 
	 * @param person
	 *          l'oggetto da rendere persistente
	 * @throws DataAccessException
	 */
	public void storePerson(final Person person) throws DataAccessException
	{
		getJdoTemplate().makePersistent(person);
	}

	/**
	 * Elimina l'oggetto passato come parametro.
	 * 
	 * @param person
	 *          oggetto da eliminare
	 * @throws DataAccessException
	 */
	public void deletePerson(final Person person) throws DataAccessException
	{
		if(person == null || person.getId() == null)
			throw new RuntimeException("Person is not persistent");
		else
			getPersistenceManager().deletePersistent(person);
	}

	/**
	 * Restituisce la lista di tutte le persone presenti in anagrafica.
	 * 
	 * @return la lista di tutte le persone presenti in anagrafica
	 * @throws DataAccessException
	 */
	public Collection<Person> getAllPersons() throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(Person.class));
	}
}
