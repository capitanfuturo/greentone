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
@Repository("documentDAO")
public class DocumentDAO extends JdoDaoSupport
{
	@Inject
	public DocumentDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	public Document loadDocument(final long id) throws DataAccessException
	{
		final Document document =
		  getJdoTemplate().getObjectById(Document.class, Long.valueOf(id));
		if(document == null)
			throw new RuntimeException("Document " + id + " not found");
		return getPersistenceManager().detachCopy(document);
	}

	public void storeDocument(final Document document) throws DataAccessException
	{
		getJdoTemplate().makePersistent(document);
	}

	public void deleteDocument(final Document document)
	  throws DataAccessException
	{
		if(document == null || document.getId() == null)
			throw new RuntimeException("Document is not persistent");
		else
			getPersistenceManager().deletePersistent(document);
	}

	public Collection<Document> getAllDocuments() throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(Document.class));
	}
}
