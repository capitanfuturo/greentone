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
 * Classe di accessso alla tabella Office nel database.
 * 
 * @author Giuseppe Caliendo
 */
@Repository("officeDAO")
public class OfficeDAO extends JdoDaoSupport {
    /**
     * Classe di accessso alla tabella Person nel database.
     * 
     * @param pmf
     *            manager della persistenza
     */
    @Inject
    public OfficeDAO(final PersistenceManagerFactory pmf) {
        setPersistenceManagerFactory(pmf);
    }

    /**
     * Restituice lo studio professionale.
     * 
     * @return lo studio professionale
     * @throws DataAccessException
     */
    public Office loadOffice() throws DataAccessException {
        final Collection<Office> offices = getJdoTemplate().find(Office.class);
        if (offices == null || offices.isEmpty())
            return new Office();
        return getPersistenceManager().detachCopy(offices.iterator().next());
    }

    /**
     * Rende persistente l'oggetto passato come parametro.
     * 
     * @param office
     *            l'oggetto da rendere persistente
     * @throws DataAccessException
     */
    public void storeOffice(final Office office) throws DataAccessException {
        getJdoTemplate().makePersistent(office);
    }
}
