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
 * Tabella di supporto per contenere property in database.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_METADATA", detachable = "true")
public class Metadata
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	String name;
	@Persistent
	String value;
	@Persistent
	MetadataType type;

	/**
	 * Restituisce l'identificativo del record.
	 * 
	 * @return l'identificativo del record
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Imposta l'identificativo del record.
	 * 
	 * @param id
	 *          l'identificativo del record
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Restituisce il nome della property.
	 * 
	 * @return il nome della property
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Imposta il nome della property.
	 * 
	 * @param name
	 *          il nome della property
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Restituisce il valore della property.
	 * 
	 * @return il valore della property
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Imposta il valore della property.
	 * 
	 * @param value
	 *          il valore della property
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Restituisce il tipo della property.
	 * 
	 * @return il tipo della property
	 */
	public MetadataType getType()
	{
		return type;
	}

	/**
	 * Imposta il tipo della property.
	 * 
	 * @param type
	 *          il tipo della property
	 */
	public void setType(MetadataType type)
	{
		this.type = type;
	}
}
