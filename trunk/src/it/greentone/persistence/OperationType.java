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
 * Tipo di operazione.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_OPERATIONTYPE", detachable = "true")
public class OperationType
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent(defaultFetchGroup = "true")
	private OperationTypeTypology typology;
	@Persistent
	private String name;
	@Persistent
	private boolean isActive;
	@Persistent
	private boolean isTaxable;

	/**
	 * Restituisce l'identificativo dell'oggetto
	 * 
	 * @return l'identificativo dell'oggetto
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Imposta l'identificativo dell'oggetto
	 * 
	 * @param id
	 *          l'identificativo dell'oggetto
	 */
	public void setId(Long id)
	{
		this.id = id;
	}


	public OperationTypeTypology getTypology()
	{
		return typology;
	}

	public void setTypology(OperationTypeTypology typology)
	{
		this.typology = typology;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean getIsActive()
	{
		return isActive;
	}

	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public boolean getIsTaxable()
	{
		return isTaxable;
	}

	public void setIsTaxable(boolean isTaxable)
	{
		this.isTaxable = isTaxable;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
