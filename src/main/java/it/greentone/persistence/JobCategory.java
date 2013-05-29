package it.greentone.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
 * L'incarico è specializzato in macro-categorie come per esempio:
 * <ul>
 * <li>Pratiche catastali</li>
 * <li>Progettazione</li>
 * <li>Stima e perizie di stima</li>
 * <li>Consulenza</li>
 * <li>Successioni</li>
 * </ul>
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_JOBCATEGORY", detachable = "true")
public class JobCategory {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    String name;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JobCategory) {
            JobCategory candidate = (JobCategory) obj;
            return id.equals(candidate.getId());
        } else
            return super.equals(obj);
    }

    /**
     * Restituisce l'identificativo del record.
     * 
     * @return l'identificativo del record
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo del record.
     * 
     * @param id
     *            l'identificativo del record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il nome della categoria dell'incarico.
     * 
     * @return il nome della categoria dell'incarico
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome della categoria dell'incarico.
     * 
     * @param name
     *            il nome della categoria dell'incarico
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
