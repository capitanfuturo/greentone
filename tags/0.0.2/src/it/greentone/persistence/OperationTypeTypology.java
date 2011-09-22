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
 * </code>
 * <br>
 * <br>
 * Ogni tipo di operazione {@link OperationType} appartiene ad una macro
 * categoria caratterizzata da un'etichetta identificativa.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_OPERATIONTYPETYPOLOGY", detachable = "true")
public class OperationTypeTypology
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof OperationTypeTypology)
		{
			OperationTypeTypology candidate = (OperationTypeTypology) obj;
			return id.equals(candidate.getId());
		}
		else
			return super.equals(obj);
	}

	/**
	 * Restituisce l'identificativo della tipologia del tipo di operazione.
	 * 
	 * @return l'identificativo della tipologia del tipo di operazione
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Imposta l'identificativo della tipologia del tipo di operazione.
	 * 
	 * @param id
	 *          l'identificativo della tipologia del tipo di operazione
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Restituisce il nome della tipologia del tipo di operazione.
	 * 
	 * @return il nome della tipologia del tipo di operazione
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Imposta il nome della tipologia del tipo di operazione
	 * 
	 * @param name
	 *          il nome della tipologia del tipo di operazione
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
